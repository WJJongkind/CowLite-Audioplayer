/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.audio.Playlist;
import cap.audio.PlaylistPlayer;
import cap.core.services.PlaylistStoreInterface;

/**
 * Default implementation of the DefaultMenuContext interface.
 * @author Wessel Jongkind
 */
class DefaultMenuContext implements DefaultMenuCoordinator.DefaultMenuContextInterface {
    
    // MARK: - Private properties
        
    private final PlaylistPlayer playlistPlayer;
    private final PlaylistStoreInterface playlistStore;

    // MARK: - Initialisers
    
    /**
     * Initialises a new DefaultMenuContext object.
     * @param playlistPlayer A player that can play playlists.
     * @param playlistStore A storage with which playlists can be persisted.
     */
    public DefaultMenuContext(PlaylistPlayer playlistPlayer, PlaylistStoreInterface playlistStore) {
        this.playlistPlayer = playlistPlayer;
        this.playlistStore = playlistStore;
    }

    @Override
    public PlaylistStoreInterface getPlaylistStore() {
        return playlistStore;
    }

    @Override
    public Playlist getCurrentPlaylist() {
        return playlistPlayer.getPlaylist();
    }

    @Override
    public String getAboutText() {
        return "";
    }

    @Override
    public String getFeaturesText() {
        return "";
    }
        
}