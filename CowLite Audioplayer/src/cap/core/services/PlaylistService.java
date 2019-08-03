/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import cap.audio.Song;
import cap.audio.files.FileSong;
import cap.audio.youtube.YouTubeSong;
import filedatareader.FileDataReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import cap.core.services.PlaylistStoreInterface;
import java.net.MalformedURLException;

/**
 *
 * @author Wessel
 */
public class PlaylistService implements PlaylistServiceInterface {
    
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
    
    private static final Pattern songEntryRegex = Pattern.compile("([a-z][a-z])-(.+)");
    private static final JFileChooser fileChooser = new  JFileChooser();
    
    // MARK: - Service methods
    
    @Override
    public Playlist loadPlayList(File file) throws IOException{
        FileDataReader reader = new FileDataReader();
        reader.setPath(file);
        
        List<String> lines = reader.getDataStringLines();
        Playlist playlist = new Playlist();
        playlist.setName(lines.get(0));
        
        for(int i = 1; i < lines.size(); i++) {
            
            Matcher matcher = songEntryRegex.matcher(lines.get(i));
            if(matcher.matches()) {
                try {
                    SongType type = SongType.lookup.get(matcher.group(1));
                    URL songUrl = new URL(matcher.group(2));
                    
                    Song song = null;
                    switch(type) {
                        case file:
                            song = new FileSong(new File(songUrl.toURI().getPath()));
                            break;
                        case YouTube:
                            try {
                                song = new YouTubeSong(songUrl);
                            }catch(Exception e) {}
                            break;
                    }
                    if(song != null) {
                        playlist.addSong(song);
                    }
                } catch (URISyntaxException ex) {
                    Logger.getLogger(PlaylistService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return playlist;
    }
    
    @Override
    public void savePlayList(Playlist playList, File target) throws FileNotFoundException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(target));
            out.println(playList.getName());
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
}
