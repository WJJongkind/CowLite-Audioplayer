/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

import cap.util.QueryReader;
import static cap.util.SugarySyntax.doTry;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with which various pieces of information can be obtained based on YouTube video URLs.
 * @author Wessel Jongkind
 */
public final class YouTubeService {
    
    // MARK: - Associated types & constants
    
    /**
     * Interface used for methods in YouTube service that support asynchronous returning
     * of requested results.
     */
    public interface PlaylistReceiver {
        
        /**
         * Notifies the receiver that a YouTube song has been loaded.
         * @param song The song that has been loaded.
         */
        public void songLoaded(YouTubeSong song);
        
    }
    
    private static class Constants {
        // TODO add support for youtu.be links
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
        
    // MARK: - Public methods
        
    /**
     * Returns the ID of the video denoted by the given YouTube URL (or null if the URL is invalid).
     * @param url The URL from which the video ID has to be obtained.
     * @return The ID of the video denoted by the given YouTube URL.
     */
    public String getVideoId(URL url) {
        String query = url.toString().substring(url.toString().indexOf("?") + 1);
        Map<String, String> queryParts = QueryReader.readQuery(query);
        return queryParts.get("v");
    }
        
    /**
     * Returns all YouTubeSongs by loading them from the list of given videoIds.
     * @param videoIds The IDs of the video IDs of which YouTubeSongs have to be obtained.
     * @return The list of YouTubeSongs that were obtained from the given video ids. Note that any videos that are not embeddable, are not included.
     * @throws IOException If the metadata of the videos could not be obtained from the YouTube API.
     */
    public List<YouTubeSong> getYouTubeSongs(String... videoIds) throws IOException { // TODO if we will non-embeddable videos, change documentation.
        ArrayList<YouTubeSong> songs = new ArrayList<>();
        getMetaData(song -> songs.add(song), videoIds);
        
        return songs;
    }
        
    /**
     * Obtains a single YouTubeSong from the given URL.
     * @param url The URL pointing to the location of the YouTube video.
     * @return A YouTubeSong representing the video at the given URL.
     * @throws IOException If the YouTubeAPI could not be reached.
     * @throws VideoNotEmbeddableException When the video is not embeddable. 
     */
    public YouTubeSong getYouTubeSongByUrl(URL url) throws IOException, VideoNotEmbeddableException { // TODO if we will non-embeddable videos, change documentation.
        String videoId = getVideoId(url);
        
        YouTube.Videos.List list = youTube.videos().list("snippet,contentDetails,status");
        list.setFields("items(snippet/title,snippet/channelTitle,contentDetails/duration,status/embeddable)");
        list.setId(videoId);
        list.setKey("AIzaSyChx2TLuzXcvmokuJkeT60fV1rDYRsS_Y8");
        VideoListResponse response = list.execute();
        
        Video video = response.getItems().get(0);
        
        if(video.getStatus().getEmbeddable()) {
            return new YouTubeSong(url, video.getSnippet().getTitle(), video.getSnippet().getChannelTitle(), videoId, Duration.parse(video.getContentDetails().getDuration()).toMillis());
        } else {
            throw new VideoNotEmbeddableException();
        }
    }
    
    /**
     * Reads the given URL and if it is a YouTube playlist, will notify the receiver of all the YouTubeSongs in that playlist. If it is a single video, 
     * notifies the receiver of that single song.
     * @param url The URL denoting the YouTube video or playlist.
     * @param receiver The receiver which can be notified if a YouTube song has been laoded from the given URL. 
     *                 May be notified several times if the URL denotes a playlist. May not be notified at all if the given URL does not contain any embeddable videos.
     */
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
                } catch (VideoNotEmbeddableException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    
    /**
     * Reads the given URL and if it is a YouTube playlist returns a list with all embeddable songs of that playlist. If the URL denotes a single video,
     * the list may contain only one item and potentially zero if the video is not embeddable.
     * @param url The URL of which all YouTube songs have to be obtained (a valid YouTube playlist or video URL).
     * @return The obtained YouTubeSongs.
     */
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
    
    // MARK: - Private methods
    
    private void readPlaylistUrl(String url, PlaylistLoaderDelegate delegate) throws IOException {
            Matcher playlistMatcher = Constants.ytPlaylistPattern.matcher(url);
            if(playlistMatcher.find()) {
                // Obtain Playlist ID
                String playlistId = playlistMatcher.group(1);
                
                // Prepare query to YouTube in order to obtain all videos in the playlist
                YouTube.PlaylistItems.List playlistItemRequest = youTube.playlistItems().list("contentDetails");
                playlistItemRequest.setPlaylistId(playlistId);
                playlistItemRequest.setFields("items(contentDetails/videoId),nextPageToken");
                playlistItemRequest.setKey("AIzaSyChx2TLuzXcvmokuJkeT60fV1rDYRsS_Y8");
                playlistItemRequest.setMaxResults(Long.valueOf(50));
                String nextToken = "";
                
                String videoIds = "";
                // Iterate over the videos in the playlist, and transform them to a usable format
                do {
                    try {
                        playlistItemRequest.setPageToken(nextToken);
                        PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                        for(PlaylistItem item : playlistItemResult.getItems()) {
                            videoIds = videoIds + item.getContentDetails().getVideoId() + ",";
                        }
                        nextToken = playlistItemResult.getNextPageToken();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                } while (nextToken != null);
                
                if(!videoIds.isEmpty()) {
                    getMetaData(delegate, videoIds.substring(0, videoIds.length() - 1).split(","));
                }
            }
    }
    
    private void getMetaData(PlaylistLoaderDelegate delegate, String... videoIds) throws IOException {
        // We have to group the videos by 50, as YT only allows obtaining data for 50 videos per result.
        String[] queryGroups = new String[(videoIds.length / 50) + (videoIds.length % 50 == 0 ? 0 : 1)];
        for(int i = 0; i < (videoIds.length / 50) + (videoIds.length % 50 == 0 ? 0 : 1); i++) {
            queryGroups[i] = String.join(",", Arrays.copyOfRange(videoIds, i * 50, Math.min((i + 1) * 50, videoIds.length)));
        }
        
        // Iterate over the various groups and obtain the video data.
        for(String queryGroup : queryGroups) {
            YouTube.Videos.List request = youTube.videos().list("snippet,contentDetails,status,id");
            request.setFields("items(snippet/title,snippet/channelTitle,contentDetails/duration,status/embeddable,id),nextPageToken");
            request.setId(queryGroup);
            request.setMaxResults(Long.valueOf(50));
            request.setKey("AIzaSyChx2TLuzXcvmokuJkeT60fV1rDYRsS_Y8");

            String nextToken = "";
            do {
                request.setPageToken(nextToken);
                VideoListResponse response = request.execute();

                for(Video video : response.getItems()) {
                    doTry(() -> {
                        delegate.songLoaded(new YouTubeSong(
                                new URL("https://www.youtube.com/watch?v=" + video.getId()), 
                                video.getSnippet().getTitle(), 
                                video.getSnippet().getChannelTitle(), 
                                video.getId(), 
                                Duration.parse(video.getContentDetails().getDuration()).toMillis()));
                    });
                }

                nextToken = response.getNextPageToken();
            }while(nextToken != null);
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
