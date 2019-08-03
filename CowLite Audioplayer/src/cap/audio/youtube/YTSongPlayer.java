/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

import cap.audio.SongPlayer;
import static cap.util.SugarySyntax.unwrappedPerform;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Callback;
import filedatareader.FileDataReader;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 *
 * @author Wessel
 */
public class YTSongPlayer implements SongPlayer<YouTubeSong> {
    
    // MARK: - Shared properties
    
    private static String videoPlayerHtml;
    
    // MARK: - Private final properties
    
    private final Browser browser = new Browser();
    private final ArrayList<WeakReference<SongPlayerObserver<YouTubeSong>>> observers = new ArrayList<>();
    
    // MARK: - Private properties
    
    private YouTubeSong currentSong;
    private double volume = 0.5;
    private PlayerState playerState = PlayerState.stopped;
    
    // MARK: - Initialisers
    
    public YTSongPlayer() throws IOException {
        if(videoPlayerHtml == null) {
            FileDataReader reader = new FileDataReader();
            reader.setPath("resources" + File.separatorChar + "html" + File.separatorChar + "YTAudioPlayer.html");
            videoPlayerHtml = reader.getDataString();
        }
    }

    @Override
    public boolean play() {
        browser.executeJavaScript("player.playVideo()");
        playerState = PlayerState.playing;
        unwrappedPerform(observers, observer -> observer.stateChanged(this, playerState));
        return true; // TODO perhaps there's a way to detect if video actually starts playing?
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
        browser.executeJavaScript("player.setVolume(" + (int)(volume * 100) + ")");
        
        unwrappedPerform(observers, observer -> observer.volumeChanged(this, volume));
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setSong(YouTubeSong song) {
        stop();
        
        currentSong = song;
        String html = videoPlayerHtml.replace("#VIDEO_PLACEHOLDER#", song.getId()).replace("#VOLUME_PLACEHOLDER#", "" + (int)(volume * 100));
        Browser.invokeAndWaitFinishLoadingMainFrame(browser, new Callback<Browser>() {
            @Override
            public void invoke(Browser t) {
                browser.loadHTML(html);
            }
        });
        
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
            unwrappedPerform(observers, observer -> observer.didSeek(this, toPosition));
        }
    }

    @Override
    public long getPosition() {
        double position = browser.executeJavaScriptAndReturnValue("player.getCurrentTime()").asNumber().getValue();
        return (long)(position * 1000);
    }

    @Override
    public long getDuration() {
        return currentSong != null ? currentSong.getDuration() : null;
    }

    @Override
    public void addObserver(SongPlayerObserver<YouTubeSong> observer) {
        observers.add(new WeakReference<>(observer));
    }

    @Override
    public void removeObserver(SongPlayerObserver<YouTubeSong> observer) {
        observers.remove(new WeakReference<>(observer));
    }
    
}
