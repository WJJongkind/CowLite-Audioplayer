/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import cap.audio.Song;
import cap.audio.files.FileSong;
import cap.audio.youtube.YouTubeService;
import cap.audio.youtube.YouTubeSong;
import static cap.util.SugarySyntax.doTry;
import filedatareader.FileDataReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

/**
 *
 * @author Wessel
 */
class LazyLoadingPlaylistService implements LazyLoadingPlaylistServiceInterface {
    
    // MARK: - Associated types & constants
    
    private enum SongType {
        file("fi"),
        YouTube("yt");
        
        private final String type;
        
        private SongType(String type) {
            this.type = type;
        }
        
        public static final Map<String, SongType> lookup = new HashMap<>();
        static {
            for(SongType songType : SongType.values()) {
                lookup.put(songType.type, songType);
            }
        }
    }
    
    // MARK: - Constants
    
    private static final Pattern songEntryRegex = Pattern.compile("([a-z][a-z])-(.+)");
    private static final JFileChooser fileChooser = new  JFileChooser();
    
    // MARK: - Private properties
    
    private final YouTubeService youTubeService = new YouTubeService();
    
    // MARK: - LazyLoadingPlaylistServiceInterface
    
    @Override
    public Playlist loadPlaylist(File file) throws IOException {
        FileDataReader reader = new FileDataReader();
        reader.setPath(file);
        
        List<String> lines = reader.getDataStringLines();
        Playlist playlist = new Playlist();
        playlist.setName(file.getName());
        
        ArrayList<Song> songs = new ArrayList<>();
        HashMap<String, Integer> indexedYouTubeSongUrls = new HashMap<>();
        
        for(int i = 0; i < lines.size(); i++) {
            Matcher matcher = songEntryRegex.matcher(lines.get(i));
            if(matcher.matches()) {
                try {
                    SongType type = SongType.lookup.get(matcher.group(1));
                    URL songUrl = new URL(matcher.group(2));
                    
                    switch(type) {
                        case file:
                            songs.add(new FileSong(new File(songUrl.toURI().getPath())));
                            break;
                        case YouTube:
                            indexedYouTubeSongUrls.put(youTubeService.getVideoId(songUrl), i);
                            break;
                    }
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        // Load YouTube songs in a batch to save costs for hitting the YT Data API
        List<YouTubeSong> youTubeSongs = youTubeService.getYouTubeSongs(indexedYouTubeSongUrls.keySet().toArray(new String[indexedYouTubeSongUrls.size()]));
        
        // We have to sort them first, because otherwise we may get an out-of-bounds exception when
        // a higher indexed song is added before lower indexed songs.
        youTubeSongs.sort(new Comparator<YouTubeSong>() {
            @Override
            public int compare(YouTubeSong firstSong, YouTubeSong secondSong) {
                return indexedYouTubeSongUrls.get(firstSong.getId()) - indexedYouTubeSongUrls.get(secondSong.getId());
            }
        });
        
        // TODO maybe we can reduce the amount of for-loops a bit....?
        for(YouTubeSong song : youTubeSongs) {
            int index = indexedYouTubeSongUrls.get(song.getId());
            songs.add(index, song);
        }
        
        for(Song song : songs) {
            playlist.addSong(song);
        }
        
        return playlist;
    }
    
    @Override
    public void savePlayList(Playlist playList, File target) throws FileNotFoundException {
        // TODO implement some sort of caching mechanism
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(target));
            for(Song song : playList.getSongs()) {
                if(song instanceof FileSong) {
                    out.println(SongType.file.type + "-" + song.getUrl().toString());
                } else if(song instanceof YouTubeSong) {
                    out.println(SongType.YouTube.type + "-" + song.getUrl().toString());
                }
            }
            out.flush();
        } catch (FileNotFoundException ex) {
            throw ex;
        } finally {
            out.close();
        }
    }
    
    // MARK: - Protected methods
    
    @Override
    public void loadPlaylist(LazyLoadablePlaylist playlist) {
        doTry(() -> {
            Playlist storedList = loadPlaylist(playlist.file);
            playlist.setSongs(storedList.getSongsInOriginalOrder());
        }); // Can't really do much in the catch clausule unfortinately
    }
    
}
