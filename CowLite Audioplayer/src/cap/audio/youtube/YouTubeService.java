/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

import cap.util.QueryReader;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Wessel
 */
public class YouTubeService {
    
    // MARK: - Associated types & constants
    
    public interface PlaylistReceiver {
        
        public void songLoaded(YouTubeSong song);
        
    }
    
    private static class Constants {
        public static final Pattern ytVideoIdPattern = Pattern.compile("\\?v=([a-zA-Z-_\\d]+)&?");
        public static final Pattern ytPlaylistPattern = Pattern.compile("&list=([a-zA-Z-_\\d]+)&?");
    }
    
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
        
    // MARK: - Methods
        
    public YouTubeSong getYouTubeSongByUrl(URL url) throws IOException, VideoNotEmbeddableException {
        String query = url.toString().substring(url.toString().indexOf("?") + 1);
        Map<String, String> queryParts = QueryReader.readQuery(query);
        String videoId = queryParts.get("v");
        
        YouTube.Videos.List list = youTube.videos().list("snippet,contentDetails,status");
        list.setFields("items(snippet/title,snippet/channelTitle,contentDetails/duration,status/embeddable)");
        list.setId(videoId);
        list.setKey("AIzaSyC2_YRcTE9916fsmA0_KRnef43GbLzz8m0");
        VideoListResponse response = list.execute();
        
        Video video = response.getItems().get(0);
        
        if(video.getStatus().getEmbeddable()) {
            return new YouTubeSong(url, video.getSnippet().getTitle(), video.getSnippet().getChannelTitle(), videoId, Duration.parse(video.getContentDetails().getDuration()).toMillis());
        } else {
            throw new VideoNotEmbeddableException();
        }
    }
    
    public void readUrl(String url, PlaylistReceiver receiver) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // Try to see if it is a playlist first. If so, obtain all videos in the playlist.
                    final Counter counter = new Counter();
                    readPlaylistUrl(url, song -> {
                        counter.count++;
                        receiver.songLoaded(song);
                    });
                    
                    if(counter.count > 0) {
                        return;
                    }
                    
                    // Try to see if it is a YouTube video url (not playlist).
                    YouTubeSong song = getYouTubeSongByUrl(new URL(url));
                    if(song != null) {
                        receiver.songLoaded(song);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    
    public List<YouTubeSong> readUrl(String url) {
        try {
            // Try to see if it is a playlist first. If so, obtain all videos in the playlist.
            List<YouTubeSong> ytPlaylist = new ArrayList<>();
            readPlaylistUrl(url, song -> ytPlaylist.add(song));
            if(!ytPlaylist.isEmpty()) {
                return ytPlaylist;
            }
            
            // Try to see if it is a YouTube video url (not playlist).
            YouTubeSong song = getYouTubeSongByUrl(new URL(url));
            if(song != null) {
                ArrayList<YouTubeSong> songList = new ArrayList<>();
                songList.add(song);
                return songList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    private void readPlaylistUrl(String url, PlaylistLoaderDelegate delegate) {
        try {
            Matcher playlistMatcher = Constants.ytPlaylistPattern.matcher(url);
            if(playlistMatcher.find()) {
                // Obtain Playlist ID
                String playlistId = playlistMatcher.group(1);
                
                // Prepare query to YouTube in order to obtain all videos in the playlist
                YouTube.PlaylistItems.List playlistItemRequest = youTube.playlistItems().list("contentDetails");
                playlistItemRequest.setPlaylistId(playlistId);
                playlistItemRequest.setFields("items(contentDetails/videoId),nextPageToken");
                playlistItemRequest.setKey("AIzaSyC2_YRcTE9916fsmA0_KRnef43GbLzz8m0");
                String nextToken = "";
                
                // Iterate over the videos in the playlist, and transform them to a usable format
                do {
                    playlistItemRequest.setPageToken(nextToken);
                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                    for(PlaylistItem item : playlistItemResult.getItems()) {
                        try {
                            delegate.songLoaded(getYouTubeSongByUrl(new URL("https://www.youtube.com/watch?v=" + item.getContentDetails().getVideoId())));
                        } catch(Exception e) {}
                    }
                    nextToken = playlistItemResult.getNextPageToken();
                } while (nextToken != null);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    // MARK: - Private associated types
    
    private interface PlaylistLoaderDelegate {
        public void songLoaded(YouTubeSong song);
    }
    
    // Required because we cannot mutate atomic properties from lambdas / inner classes
    private class Counter {
        int count = 0;
    }
    
}
