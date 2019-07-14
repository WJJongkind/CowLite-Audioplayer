/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio;

import java.net.URL;

/**
 *
 * @author Wessel
 */
public interface Song {
    public static final String unknownPlaceholder = "Unknown";
    public String getSongName();
    public String getAlbumName();
    public String getArtistName();
    public URL getUrl();
    public long getDuration();
}
