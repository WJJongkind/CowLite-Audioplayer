/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wessel
 */
public class YouTubePlaylistLoader 
{
        private static YouTube getYouTubeService() throws IOException {
        return new YouTube.Builder(
        new NetHttpTransport(), new JacksonFactory(), 
                new HttpRequestInitializer()
                {
            @Override
            public void initialize(HttpRequest hr)  {}
            })
            .setApplicationName("CowLite Audioplayer")
            .build();
    }

    public static Map<String, PlaylistItem> getYouTubePlaylistVideos(String playlistID) throws Exception 
    {
        Map<String, PlaylistItem> playlistItemList = new HashMap<String, PlaylistItem>();
        YouTube youtube = getYouTubeService();
        
        YouTube.PlaylistItems.List playlistItemRequest = youtube.playlistItems().list("snippet,contentDetails");
        playlistItemRequest.setPlaylistId(playlistID);
        playlistItemRequest.setFields("items(snippet/title,snippet/description,snippet/thumbnails/default/url,contentDetails/videoId),nextPageToken,pageInfo");
        playlistItemRequest.setKey("AIzaSyC2_YRcTE9916fsmA0_KRnef43GbLzz8m0");
        String nextToken = "";
        do {
            playlistItemRequest.setPageToken(nextToken);
            PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

            for(PlaylistItem item : playlistItemResult.getItems())
                playlistItemList.put((String) item.getContentDetails().get("videoId"), item);
            
            nextToken = playlistItemResult.getNextPageToken();
            
        } while (nextToken != null);
        
        return playlistItemList;
    }
}
