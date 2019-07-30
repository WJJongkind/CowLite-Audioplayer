/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio;

import cap.core.audio.files.FileSongPlayer;
import cap.core.audio.youtube.YTSongPlayer;
import cap.core.audio.files.FileSong;
import cap.core.audio.youtube.YouTubeSong;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 *
 * @author Wessel
 */
public class DynamicSongPlayer implements SongPlayer<Song> {
    
    private final FileSongPlayer fileSongPlayer;
    private final YTSongPlayer ytSongPlayer;
    
    private ArrayList<WeakReference<SongPlayerObserver<Song>>> observers = new ArrayList<>();
    private SongPlayer<?> activePlayer;
    private double volume = 0.5;
    
    public DynamicSongPlayer() throws IOException {
        fileSongPlayer = new FileSongPlayer();
        ytSongPlayer = new YTSongPlayer();
    }

    @Override
    public boolean play() {
        PlayerState oldState = getPlayerState();
        boolean result = activePlayer == null ? false : activePlayer.play();
        System.out.println(getPlayerState() + "   " + oldState);
        if(getPlayerState() != oldState) {
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
        
        if(activePlayer != null) {
            activePlayer.setVolume(volume);
        }
        
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
            unwrappedPerform(observers, observer -> observer.didSeek(this, toPosition));
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
    
}
