/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio;

import cap.core.audio.youtube.YTAudioPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Wessel
 */
public class AudioController 
{
    private AudioPlayer player;
    private final Map<String, String> PLAYLISTS;
    private final Map<String, String> SETTINGS;
    private String loadedList;
    
    public AudioController(Map<String, String> playlists, Map<String, String> settings)
    {
        this.PLAYLISTS = playlists;
        this.SETTINGS = settings;
    }
    
    public void addPlaylist(String name, String identifier)
    {
        PLAYLISTS.put(name, identifier);
    }
    
    public void removePlaylist(String name)
    {
        PLAYLISTS.remove(name);
    }
    
    public void loadPlaylist(String playlist)
    {
        System.out.println("Load event");
        if(player != null && playlist.equals(loadedList) && !player.getList().isEmpty())
            return;
        
        System.out.println("loading...");
        loadedList = playlist;
        
        if(player != null)
            player.stop();
        
        File file = new File(PLAYLISTS.get(playlist));
        if(file.exists())
            player = new FileAudioPlayer(SETTINGS);
        else
            player = new YTAudioPlayer(SETTINGS);
        
        player.loadList(PLAYLISTS.get(playlist));
    }
    
    public void loadRandomList()
    {
        ArrayList<String> playlists = new ArrayList<>(PLAYLISTS.keySet());
        loadPlaylist(playlists.get(new Random().nextInt(playlists.size())));
    }
    
    public void loadFileAudioPlayer()
    {
        loadedList = null;
        if(player != null)
            player.stop();
        player = new FileAudioPlayer(SETTINGS);
    }
    
    public AudioPlayer getPlayer()
    {
        return player;
    }
    
    public String getPlaylistPath(String name)
    {
        return PLAYLISTS.get(name);
    }
    
    public Map<String, String> getPlaylists()
    {
        return PLAYLISTS;
    }
    
    public boolean playlistExists(String path, String name)
    {
        return PLAYLISTS.containsKey(name) || PLAYLISTS.containsValue(path);
    }
    
    public int getPlaylistCount()
    {
        return PLAYLISTS.size();
    }
}
