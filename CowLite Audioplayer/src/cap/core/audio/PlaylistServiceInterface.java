/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Wessel
 */
public interface PlaylistServiceInterface {
    public Playlist loadPlayList(File file) throws IOException;
    public void savePlayList(Playlist playList, File target) throws FileNotFoundException;
}
