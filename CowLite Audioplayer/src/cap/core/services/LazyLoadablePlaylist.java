/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import cap.audio.Song;
import static cap.util.SugarySyntax.doTry;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Wessel
 */
class LazyLoadablePlaylist extends Playlist {
    
    // MARK: - Associated types
    
    protected enum State {
        unloaded,
        loading,
        loaded;
    }
    
    // MARK: - Protected properties
    
    protected final File file;
    
    // MARK: - Private properties
    
    private final CountDownLatch latch = new CountDownLatch(1);
    private final LazyLoadingPlaylistServiceInterface playlistService;
    
    private State state = State.unloaded;
    
    // MARK: - Initialisers
    
    LazyLoadablePlaylist(String name, File file, LazyLoadingPlaylistServiceInterface playlistService) {
        super();
        super.setName(name);
        this.file = file;
        this.playlistService = playlistService;
    }
    
    // MARK: - Playlist
    
    @Override
    public void addSong(Song song) {
        load();
        super.addSong(song);
    }
    
    @Override
    public List<Song> getSongsInOriginalOrder() {
        load();
        return super.getSongsInOriginalOrder();
    }
    
    @Override
    public List<Song> getSongs() {
        load();
        return super.getSongs();
    }
    
    @Override
    public Song getSong(int index) {
        load();
        return super.getSong(index);
    }
    
    // MARK: - Protected methods
    
    protected void setSongs(List<Song> songs) {
        for(Song song : songs) {
            super.addSong(song);
        }
    }
    
    protected boolean setState(State state) {
        synchronized(state) {
            if(this.state.compareTo(state) >= 0) {
                return false;
            }

            this.state = state;
            if(state == State.loaded) {
                latch.countDown();
            }

            return true;
        }
    }
    
    protected State getState() {
        synchronized(state) {
            return state;
        }
    }
    
    // MARK: - Private methods
    
    private void load() {
        doTry(() -> {
            switch(getState()) {
                case unloaded:
                    if(setState(State.loading)) {
                        playlistService.loadPlaylist(this);
                        setState(State.loaded);
                    }
                    latch.countDown();
                    break;
                case loading:
                    latch.await();
                    setState(State.loaded);
                    break;
                case loaded:
                    break;
            }
        });
    }
    
}
