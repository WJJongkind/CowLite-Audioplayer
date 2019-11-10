/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio;

import java.net.URL;

/**
 * A Song is a type representing a real-world song by denoting it's name, album, artist, a URL with which
 * the source can be located and it's duration.
 * @author Wessel Jongkind
 */
public interface Song {
    
    /**
     * This value will be used whenever a piece of metadata of the song is unknown.
     */
    public static final String unknownPlaceholder = "Unknown";
    
    /**
     * Returns the title of the song.
     * @return The title of the song.
     */
    public String getSongName();
    
    /**
     * Returns the album of the song.
     * @return The album of the song.
     */
    public String getAlbumName();
    
    /**
     * Returns the artist of the song.
     * @return The artist of the song.
     */
    public String getArtistName();
    
    /**
     * Returns the url at which the source of the song can be located.
     * @return The url denoting the source of the song.
     */
    public URL getUrl();
    
    /**
     * Returns the duration of the song in milliseconds.
     * @return The duration of the song in milliseconds.
     */
    public long getDuration();
}
