/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.files;

import cap.audio.files.FileSongMetaDataReader.MetaData;
import cap.audio.Song;
import static cap.util.SugarySyntax.nilCoalesce;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implementation of the Song interface used for referencing songs that are present on the local file-system.
 * @author Wessel Jongkind
 */
public class FileSong implements Song {
    
    // MARK: - Constants
    
    private static final FileSongMetaDataReader reader = new FileSongMetaDataReader();
    
    // MARK: - Private properties
    
    private final long duration;
    private final String songName;
    private final String albumName;
    private final String artistName;
    private final URL url;
    
    /**
     * Instantiates a new FileSong from the given File.
     * @param file The File denoting the location at which the song can be found.
     * @throws MalformedURLException Should in practice never happen, but may occur when the given File cannot be converted to a URL.
     */
    public FileSong(File file) throws MalformedURLException {
        this.url = file.toURI().toURL();
        
        MetaData metaData = reader.readMetaData(this.url);
        this.songName = nilCoalesce(metaData.song, file.getName());
        this.albumName = parseMetadataValue(metaData.album);
        this.artistName = parseMetadataValue(metaData.artist);
        this.duration = metaData.duration;
    }
    
    private String parseMetadataValue(String value) {
        if(value == null || value.trim().equals("")) {
            return unknownPlaceholder;
        } else {
            return value;
        }
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getSongName() {
        return songName;
    }

    @Override
    public String getAlbumName() {
        return albumName;
    }

    @Override
    public String getArtistName() {
        return artistName;
    }

    @Override
    public URL getUrl() {
        return url;
    }
    
}
