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
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * DynamicSongPlayer is a type which can play multiple types of Song objects. Currently it supports playing:
 * 
 * 1. FileSong
 * 2. YouTubeSong
 * 
 * @author Wessel Jongkind
 */
public class DynamicSongPlayer implements SongPlayer<Song> {
    
    // MARK: - Private properties
    
    private final ArrayList<WeakReference<SongPlayerObserver<Song>>> observers = new ArrayList<>();
    private final FileSongPlayer fileSongPlayer;
    private final YTSongPlayer ytSongPlayer;
    
    private SongPlayer<?> activePlayer;
    private double volume = 0.5;
    
    // MARK: - Initialisers
    
    /**
     * Instantiates a new DynamicSongPlayer.
     * @throws IOException If one of the underlying players could not be loaded.
     */
    public DynamicSongPlayer() throws IOException {
        fileSongPlayer = new FileSongPlayer();
        ytSongPlayer = new YTSongPlayer();
    }
    
    // MARK: - SongPlayer

    @Override
    public void play() {
        if(activePlayer != null) {
            activePlayer.play();
        }
    }

    @Override
    public void pause() {
        if(activePlayer != null) {
            activePlayer.pause();
        }
    }

    @Override
    public void stop() {
        if(activePlayer != null) {
            activePlayer.stop();
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
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setSong(Song song) {
        stop();
        
        if(song instanceof FileSong) {
            swapObservers(ytSongPlayer, fileSongPlayer);
            fileSongPlayer.setSong((FileSong) song);
            activePlayer = fileSongPlayer;
        } else if(song instanceof YouTubeSong) {
            swapObservers(fileSongPlayer, ytSongPlayer);
            ytSongPlayer.setSong((YouTubeSong) song);
            activePlayer = ytSongPlayer;
        } else if(song == null && activePlayer != null) {
            activePlayer.setSong(null);
        }
    }

    @Override
    public Song getSong() {
        return activePlayer == null ? null : activePlayer.getSong();
    }

    @Override
    public void seek(long toPosition) {
        if(activePlayer != null && activePlayer.getSong() != null) {
            activePlayer.seek(toPosition);
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
    
    // MARK: - Private methods
    
    private void swapObservers(SongPlayer oldPlayer, SongPlayer newPlayer) {
        Iterator<WeakReference<SongPlayerObserver<Song>>> observerIterator = observers.iterator();
        
        while(observerIterator.hasNext()) {
            WeakReference<SongPlayerObserver<Song>> nextObserverRef = observerIterator.next();
            SongPlayerObserver<Song> observer = nextObserverRef.get();
            if(observer == null) {
                observerIterator.remove();
            } else {
                oldPlayer.removeObserver(observer);
                newPlayer.addObserver(observer);
            }
        }
    }
    
}
