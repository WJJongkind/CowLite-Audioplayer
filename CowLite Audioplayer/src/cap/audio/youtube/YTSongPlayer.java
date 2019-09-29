/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

import cap.audio.Song;
import cap.audio.SongPlayer;
import cap.util.ActivityQueue;
import cap.util.ActivityQueue.ActivityToken;
import cap.util.HeadlessBrowser;
import static cap.util.SugarySyntax.tryParseInt;
import static cap.util.SugarySyntax.unwrappedPerform;
import filedatareader.FileDataReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Wessel
 */
public class YTSongPlayer implements SongPlayer<YouTubeSong> {
    
    // MARK: - Associated types & constants
    
    private static final class Constants {
        public static final String readyMessage = "CowLite-READY";
        public static final String stateMessage = "CowLite-STATE";
    }

    
    // MARK: - Shared properties
    
    private static String videoPlayerHtml;
    
    // MARK: - Private final properties
    
    private final ActivityQueue activityQueue = new ActivityQueue();
    private final HeadlessBrowser browser = new HeadlessBrowser();
    private final ArrayList<WeakReference<SongPlayerObserver<YouTubeSong>>> observers = new ArrayList<>();
    private final YTSocket ytSocket = new YTSocket();
    
    // MARK: - Private properties
    
    private YouTubeSong currentSong;
    private PlayerState playerState = PlayerState.stopped;
    private ActivityToken playSongToken;
    private ActivityToken setSongToken;
    private boolean isFinished = false;
    private double volume = 0.5;
    
    
    // MARK: - Initialisers
    
    public YTSongPlayer() throws IOException {
        if(videoPlayerHtml == null) {
            FileDataReader reader = new FileDataReader();
            reader.setPath("resources" + File.separatorChar + "html" + File.separatorChar + "YTAudioPlayer.html");
            videoPlayerHtml = reader.getDataString();
        }
        
        ytSocket.start();
        activityQueue.start();
    }

    @Override
    public void play() {
        if(playSongToken != null) {
            playSongToken.cancelActivity();
        }
        
        if(currentSong == null) {
            return;
        }
        
        activityQueue.submitActivity(() -> {
            browser.setConsoleListener(message -> {
                Integer value;
                if(message != null && message.contains(Constants.stateMessage) && (value = tryParseInt(message.replace(Constants.stateMessage, ""))) != null) {
                    switch(value) {
                        case 0:
                            isFinished = true;
                            break;
                        case 1:
                        case 3:
                            if(playerState == PlayerState.playing) {
                                return;
                            }
                            playerState = PlayerState.playing;
                            unwrappedPerform(observers, observer -> observer.stateChanged(this, playerState));
                    }
                }
            });
            browser.executeJavaScript("play()");
        });
    }

    @Override
    public void pause() {
        if(playerState == PlayerState.playing) {
            playerState = PlayerState.paused;
            browser.executeJavaScript("player.pauseVideo()");
            
            unwrappedPerform(observers, observer -> observer.stateChanged(this, playerState));
        }
    }

    @Override
    public void stop() {
        if(playerState == PlayerState.playing || playerState == PlayerState.paused) {
            playerState = PlayerState.stopped;
            browser.executeJavaScript("player.pauseVideo()");
            browser.executeJavaScript("player.seekTo(0, true)");
            
            unwrappedPerform(observers, observer -> observer.stateChanged(this, playerState));
        }
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
        browser.executeJavaScript("setVolume(" + (int)(volume * 100) + ")");

        unwrappedPerform(observers, observer -> observer.volumeChanged(this, volume));
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setSong(YouTubeSong song) {
        if(setSongToken != null) {
            setSongToken.cancelActivity();
        }
        if(playSongToken != null) {
            setSongToken.cancelActivity();
        }
        
        setSongToken = activityQueue.submitActivity(() -> {
            stop();
            currentSong = song;

            if(song == null) {
                return;
            }

            String html = videoPlayerHtml.replace("#VIDEO_PLACEHOLDER#", song.getId()).replace("#VOLUME_PLACEHOLDER#", "" + (int)(volume * 100));
            ytSocket.setHTML(html);

            CountDownLatch latch = new CountDownLatch(1);
            browser.setConsoleListener((message) -> {
                if(Constants.readyMessage.equals(message)) {
                    latch.countDown();
                }
            });
            browser.loadWebPage("http://localhost:6969");
            browser.executeJavaScript("loadIframeAPI()");
            isFinished = false;

            try { latch.await(); } catch (InterruptedException ex) {} 
        });
        
        // Because the setting of songs and playing of songs is handled on the activityQueue, it is
        // fine to already notify observers that the song is changed.
        unwrappedPerform(observers, observer -> observer.songChanged(this, song));
    }

    @Override
    public YouTubeSong getSong() {
        return currentSong;
    }

    @Override
    public void seek(long toPosition) {
        if(currentSong != null) {
            double position = toPosition / 1000.0;
            browser.executeJavaScript("player.seekTo(" + position + ", true)");
            unwrappedPerform(observers, observer -> observer.positionChanged(this, toPosition));
        }
    }

    @Override
    public long getPosition() {
        // Avoids hypothetical nullpointerexception where null check says song isn't null, but then when obtaining duration it is null.
        // This may happen due to the usage of ActivityQueue.
        Song currentSong = this.currentSong;
        
        if(currentSong == null) {
            return -1;
        } else {
            Number playerTime = browser.executeJavaScript("player.getCurrentTime()");
            
            if(playerTime == null) {
                return -1;
            }
            
            return isFinished ? currentSong.getDuration() : Math.round(playerTime.doubleValue() * 1000);
        }
    }

    @Override
    public long getDuration() {
        // Avoids hypothetical nullpointerexception where null check says song isn't null, but then when obtaining duration it is null.
        // This may happen due to the usage of ActivityQueue.
        Song currentSong = this.currentSong;
        return currentSong != null ? currentSong.getDuration() : -1;
    }

    @Override
    public void addObserver(SongPlayerObserver<YouTubeSong> observer) {
        observers.add(new WeakReference<>(observer));
    }

    @Override
    public void removeObserver(SongPlayerObserver<YouTubeSong> observer) {
        observers.remove(new WeakReference<>(observer));
    }
    
    // MARK: - Private associated types
    
    // Required because YouTube blocks playing some videos in browsers when they are not
    // being played from a domain (e.g. when playing a YT video from a .html file).
    // We trick YouTube by hosting the video on localhost... Sucks but is required for
    // a reliable experience.
    private class YTSocket extends Thread {
        
        private final ServerSocket serverSocket;
        private String html = "";
        
        public YTSocket() throws IOException {
            serverSocket = new ServerSocket(6969);
        }
        
        @Override
        public void run() {
            while(true) {
                try {
                    Socket client = null;
                    while((client = serverSocket.accept()) != null) {
                        try (PrintWriter out = new PrintWriter(client.getOutputStream())) {
                            out.println("HTTP/1.1 200 OK");
                            out.println("Content-Type: text/html; charset: utf8");
                            out.println("\r\n");
                            synchronized(html) {
                                out.println(html);
                            }
                            out.flush();
                            out.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        client.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        public void setHTML(String html) {
            synchronized(this.html) {
                this.html = html;
            }
        }
        
    }
    
}
