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
 *
 * @author Wessel
 */
interface PlaylistServiceInterface {
    
    Playlist loadPlaylist(File file) throws IOException;
    void savePlayList(Playlist playList, File target) throws FileNotFoundException;
    
}
