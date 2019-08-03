/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.files;

import cap.audio.files.FileSongMetaDataReader.MetaData;
import cap.audio.Song;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 *
 * @author Wessel
 */
public class FileSong implements Song {
    
    // MARK: - Constants
    
    private static final MediaPlayer mediaPlayer = new MediaPlayerFactory().mediaPlayers().newMediaPlayer();
    
    // MARK: - Private properties
    
    private final long duration;
    private final String songName;
    private final String albumName;
    private final String artistName;
    private final URL url;
    
    public FileSong(File file) throws MalformedURLException {
        this.url = file.toURI().toURL();
        
        synchronized(mediaPlayer) {
            MetaData metaData;
            mediaPlayer.media().prepare(this.url.getProtocol() + "://" + this.url.getPath());
            metaData = new FileSongMetaDataReader().readMetaData(mediaPlayer);
            
            if(metaData != null) {
                this.songName = parseMetadataValue(metaData.song);
                this.albumName = parseMetadataValue(metaData.album);
                this.artistName = parseMetadataValue(metaData.artist);
                this.duration = metaData.duration;
            } else {
                this.songName = file.getName();
                this.albumName = unknownPlaceholder;
                this.artistName = unknownPlaceholder;
                this.duration = mediaPlayer.media().info().duration();
            }
        }
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
