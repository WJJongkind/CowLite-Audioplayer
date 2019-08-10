/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio;

import cap.audio.files.FileSongPlayer;
import cap.audio.youtube.YTSongPlayer;
import cap.audio.files.FileSong;
import cap.audio.youtube.YouTubeSong;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author Wessel
 */
public class DynamicSongPlayer implements SongPlayer<Song> {
    
    // MARK: - Constants
    
    private static final class Constants {
        public static final int positionChangedTimerInterval = 30;
    }
    
    // MARK: - Private properties
    
    private final ArrayList<WeakReference<SongPlayerObserver<Song>>> observers = new ArrayList<>();
    private final FileSongPlayer fileSongPlayer;
    private final YTSongPlayer ytSongPlayer;
    private final Timer timer;
    
    private SongPlayer<?> activePlayer;
    private double volume = 0.5;
    
    // MARK: - Initialisers
    
    public DynamicSongPlayer() throws IOException {
        fileSongPlayer = new FileSongPlayer();
        ytSongPlayer = new YTSongPlayer();
        timer = new Timer(Constants.positionChangedTimerInterval, e -> notifyPositionChanged());
    }
    
    // MARK: - SongPlayer

    @Override
    public boolean play() {
        PlayerState oldState = getPlayerState();
        boolean result = activePlayer == null ? false : activePlayer.play();
        if(getPlayerState() != oldState) {
            timer.start();
            unwrappedPerform(observers, observer -> observer.stateChanged(this, getPlayerState()));
        }
        return result;
    }

    @Override
    public void pause() {
        if(activePlayer != null) {
            PlayerState oldState = getPlayerState();
            activePlayer.pause();
            if(getPlayerState() != oldState) {
                timer.stop();
                unwrappedPerform(observers, observer -> observer.stateChanged(this, getPlayerState()));
            }
        }
    }

    @Override
    public void stop() {
        if(activePlayer != null) {
            PlayerState oldState = getPlayerState();
            activePlayer.stop();
            if(getPlayerState() != oldState) {
                timer.stop();
                unwrappedPerform(observers, observer -> observer.stateChanged(this, getPlayerState()));
            }
        }
    }

    @Override
    public PlayerState getPlayerState() {
        if(activePlayer != null) {
            return activePlayer.getPlayerState();
        }
        
        return PlayerState.stopped;
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
        
        fileSongPlayer.setVolume(volume);
        ytSongPlayer.setVolume(volume);
        
        unwrappedPerform(observers, observer -> observer.volumeChanged(this, volume));
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setSong(Song song) {
        stop();
        
        if(song instanceof FileSong) {
            fileSongPlayer.setSong((FileSong) song);
            activePlayer = fileSongPlayer;
        } else if(song instanceof YouTubeSong) {
            ytSongPlayer.setSong((YouTubeSong) song);
            activePlayer = ytSongPlayer;
        } else if(song == null && activePlayer != null) {
            activePlayer.setSong(null);
        }
        
        unwrappedPerform(observers, observer -> observer.songChanged(this, song));
    }

    @Override
    public Song getSong() {
        return activePlayer == null ? null : activePlayer.getSong();
    }

    @Override
    public void seek(long toPosition) {
        if(activePlayer != null && activePlayer.getSong() != null) {
            activePlayer.seek(toPosition);
            unwrappedPerform(observers, observer -> observer.positionChanged(this, toPosition));
        }
    }

    @Override
    public long getPosition() {
        return activePlayer == null ? -1 : activePlayer.getPosition();
    }

    @Override
    public long getDuration() {
        return activePlayer == null ? -1 : activePlayer.getDuration();
    }

    @Override
    public void addObserver(SongPlayerObserver<Song> observer) {
        observers.add(new WeakReference<>(observer));
    }

    @Override
    public void removeObserver(SongPlayerObserver<Song> observer) {
        observers.remove(new WeakReference<>(observer));
    }
    
    // MARK: - ActionListener for timer
    
    private void notifyPositionChanged() {
        unwrappedPerform(observers, observer -> observer.positionChanged(this, getPosition()));
    }
    
}
