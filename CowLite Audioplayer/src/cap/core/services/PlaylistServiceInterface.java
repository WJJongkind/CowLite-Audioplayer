/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface for types within the cap.core.services package to load playlists from a file
 * and to store playlists to a file. This is the layer between the file system and the store
 * itself.
 * @author Wessel Jongkind
 */
interface PlaylistServiceInterface {
    
    /**
     * Loads a playlist from the given file. If a file is corrupted or invalid,
     * behavior of this function varies based on the implementation.
     * @param file The file from which a playlist has to be loaded.
     * @return The loaded playlist.
     * @throws IOException When the given file could not be read.
     */
    Playlist loadPlaylist(File file) throws IOException;
    
    /**
     * Saves a playlist to the given file. Note that the file needs to exist. Otherwise
     * an exception will be thrown.
     * @param playList The playlist which needs to be saved to a file.
     * @param target The file to which the playlist needs to be saved.
     * @throws FileNotFoundException When the given target file does not exist.
     */
    void savePlayList(Playlist playList, File target) throws FileNotFoundException;
    
}
