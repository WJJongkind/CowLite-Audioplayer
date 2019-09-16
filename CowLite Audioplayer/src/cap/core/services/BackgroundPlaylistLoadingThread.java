/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import java.util.List;

/**
 *
 * @author Wessel
 */
public final class BackgroundPlaylistLoadingThread extends Thread {
    
    // MARK: - Private properties
    
    private final LazyLoadingPlaylistServiceInterface playlistService;
    private final List<LazyLoadablePlaylist> playlists;
    
    // MARK: - Initialisers
    
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
