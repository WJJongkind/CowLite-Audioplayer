/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

import cap.audio.Song;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Wessel
 */
public class YouTubeSong implements Song {
    
    // MARK: - Constants
    
    private static final class Constants {
        public static final YouTubeService youtubeService = new YouTubeService();
    }
    
    // MARK: - Private properties

    private final long duration;
    private final String songName;
    private final String albumName;
    private final String artistName;
    private final URL url;
    private final String videoId;
    
    // MARK: - Public Initialisers
    
    public YouTubeSong(URL url) throws IOException, VideoNotEmbeddableException {
        YouTubeSong info = Constants.youtubeService.getYouTubeSongByUrl(url);
        this.duration = info.duration;
        this.songName = info.songName;
        this.albumName = info.albumName;
        this.artistName = info.artistName;
        this.url = info.url;
        this.videoId = info.videoId;
    }
    
    // MARK: - Package-private initialisers

    YouTubeSong(URL url, String songName, String artistName, String videoId, long duration) {
        this.songName = songName;
        this.artistName = artistName;
        this.duration = duration;
        this.videoId = videoId;
        this.albumName = unknownPlaceholder;
        this.url = url;
    }
    
    // MARK: - YouTubeSong specific methods
    
    public String getId() {
        return videoId;
    }
    
    // MARK: - Song
    
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

    @Override
    public long getDuration() {
        return duration;
    }
    
}
