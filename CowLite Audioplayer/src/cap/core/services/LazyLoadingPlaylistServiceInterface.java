/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

/**
 * Protocol for a special subtype of PlaylisServices that allow for lazy loading
 * of playlists.
 * @author Wessel Jongkind
 */
interface LazyLoadingPlaylistServiceInterface extends PlaylistServiceInterface {
    
    /**
     * Loads the contents of the given playlist. During loading, the playlist's state
     * will be set to "loading". Once the playlist has been fully loaded, it's state will be
     * set to "loaded".
     * @param playlist The playlist that needs to be loaded.
     */
    void loadPlaylist(LazyLoadablePlaylist playlist);
    
}
