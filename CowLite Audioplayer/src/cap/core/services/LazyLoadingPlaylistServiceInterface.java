/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

/**
 *
 * @author Wessel
 */
interface LazyLoadingPlaylistServiceInterface extends PlaylistServiceInterface {
    
    void loadPlaylist(LazyLoadablePlaylist playlist);
    
}
