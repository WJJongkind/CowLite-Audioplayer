/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio.youtube;

import cap.core.audio.Song;
import cap.util.QueryReader;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

/**
 *
 * @author Wessel
 */
public class YouTubeSong implements Song {
    
    // MARK: - Shared properties
    
    private static final YouTube youTube = 
            new YouTube.Builder(
                    new NetHttpTransport(), 
                    new JacksonFactory(), 
                    new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest hr)  {}
                    }
            ).setApplicationName("CowLite Audioplayer").build();
    
    // MARK: - Private properties

    private final long duration;
    private final String songName;
    private final String albumName;
    private final String artistName;
    private final URL url;
    private final String videoId;
    
    // MARK: - Initialisers
    
    public YouTubeSong(URL url) throws IOException {
        this.url = url;
        albumName = unknownPlaceholder;
        
        String query = url.toString().substring(url.toString().indexOf("?") + 1);
        Map<String, String> queryParts = QueryReader.readQuery(query);
        videoId = queryParts.get("v");
        
        YouTube.Videos.List list = youTube.videos().list("snippet,contentDetails, status");
        list.setFields("items(snippet/title,snippet/description,snippet/channelTitle,contentDetails/duration,status/embeddable)");
        list.setId(videoId);
        list.setKey("AIzaSyC2_YRcTE9916fsmA0_KRnef43GbLzz8m0");
        VideoListResponse response = list.execute();
        
        Video video = response.getItems().get(0);
        
        if(video.getStatus().getEmbeddable()) {
            songName = video.getSnippet().getTitle();
            artistName = video.getSnippet().getChannelTitle();
            duration = Duration.parse(video.getContentDetails().getDuration()).toMillis();
            System.out.println("Loaded with: " + songName + ", " + artistName + ", " + duration);
        } else {
            throw new IllegalArgumentException("Video is not embeddable");
        }
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
