/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface for services that provide playlist storage and retrieval capabilities. All
 * implementers of this interface *should* notify their observers based on the methods defined
 * in PlayliststoreObserver protocol.
 * @author Wessel Jongkind
 */
public interface PlaylistStoreInterface {
    
    // MARK: - Associated types
    
    /**
     * Interface for observers of a playlist storage. 
     */
    public interface PlaylistStoreObserver {
        
        /**
         * Notifies the observer that a playlist has been removed from the store.
         * @param sender The sender of the notification.
         * @param playlist The playlist that was removed.
         */
        public void didRemovePlaylist(PlaylistStoreInterface sender, Playlist playlist);
        public void didAddPlaylist(PlaylistStoreInterface sender, Playlist playlist);
    }
    
    // MARK: - Interface
    
    /**
     * Adds a playlist to the store. Persistence depends on the actual implementation of the store.
     * @param playlist The playlist to be stored.
     * @param file The file at which the playlist has to be stored.
     * @throws IOException When the playlist could not be stored due to an exception.
     */
    public void addPlaylist(Playlist playlist, File file) throws IOException;
    
    /**
     * Removes a playlist from the store.
     * @param playlist The playlist that needs to be removed.
     * @throws IOException  When the playlist could not be removed due to an exception.
     */
    public void removePlaylist(Playlist playlist) throws IOException;
    
    /**
     * Returns all playlists that this store contains.
     * @return All playlists that this store contains.
     */
    public List<Playlist> getPlaylists();
    
    /**
     * Adds an observer to the playlist store, which will then be notified about
     * events. Note that the implementation of the PlaylistStoreInterface may or may not
     * strongly retain the observer.
     * @param observer The observer that needs to be added.
     */
    public void addObserver(PlaylistStoreObserver observer);
    
}
