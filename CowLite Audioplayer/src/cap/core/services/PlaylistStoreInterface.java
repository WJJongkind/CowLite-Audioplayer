/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.core.audio.Playlist;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Wessel
 */
public interface PlaylistStoreInterface {
    
    // MARK: - Associated types
    
    public interface PlaylistStoreObserver {
        public void didRemovePlaylist(PlaylistStoreInterface sender, Playlist playlist);
        public void didAddPlaylist(PlaylistStoreInterface sender, Playlist playlist);
    }
    
    // MARK: - Interface
    
    public void addPlaylist(Playlist playlist, URL url) throws IOException;
    public void removePlaylist(Playlist playlist) throws IOException;
    public List<Playlist> getPlaylists();
    public void addObserver(PlaylistStoreObserver observer);
    
}
