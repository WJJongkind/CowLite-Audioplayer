/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import java.util.List;

/**
 * Processing thread for loading lazily loadable playlists.
 * @author Wessel Jongkind
 */
final class BackgroundPlaylistLoadingThread extends Thread {
    
    // MARK: - Private properties
    
    private final LazyLoadingPlaylistServiceInterface playlistService;
    private final List<LazyLoadablePlaylist> playlists;
    
    // MARK: - Initialisers
    
    /**
     * Initialises a new BackgroundPlaylistLoadingThread.
     * @param playlistService A service with which LazyLoadablePlaylists can be loaded.
     * @param playlists The playlists that need to be loaded. Note that if the playlist's state is
     * already set to "loaded" before this thread processes it, then it will not be handled by this thread.
     */
    public BackgroundPlaylistLoadingThread(LazyLoadingPlaylistServiceInterface playlistService, List<LazyLoadablePlaylist> playlists) {
        this.playlistService = playlistService;
        this.playlists = playlists;
    }
    
    // MARK: - Thread
    
    @Override
    public void run() {
        for(LazyLoadablePlaylist playlist : playlists) {
            if(playlist.setState(LazyLoadablePlaylist.State.loading)) {
                playlistService.loadPlaylist(playlist);
                playlist.setState(LazyLoadablePlaylist.State.loaded);
            }
        }
    }
    
}
