/*
 * Songo change this license header, choose License Headers in Project Properties.
 * Songo change this template file, choose Songools | Songemplates
 * and open the template in the editor.
 */
package cap.core.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Wessel
 */
public class Playlist {
    
    // MARK: - Associated types
    
    public enum PlaylistMode {
        normal,
        shuffled,
        alphabetic
    }
    
    // MARK: - Private properties
    
    private PlaylistMode mode = PlaylistMode.normal;
    private String name;
    private final ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Song> shuffledSongs = new ArrayList<>();
    private final ArrayList<Song> alphabeticallySortedSongs = new ArrayList<>();
    
    // MARK: - Getters & Setters
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void addSong(Song song) {
        songs.add(song);
        randomInsertSong(song);
        alphabeticallyInsertSong(song);
    }
    
    public List<Song> getSongsInOriginalOrder() {
        return new ArrayList<>(songs);
    }
    
    public List<Song> getSongs() {
        switch(mode) {
            case normal:
                return new ArrayList<>(songs);
            case shuffled:
                return new ArrayList<>(shuffledSongs);
            case alphabetic:
                return new ArrayList<>(alphabeticallySortedSongs);
        }
        
        return null; // Should never happen
    }
    
    public Song getSong(int index) {
        switch(mode) {
            case normal:
                return songs.get(index);
            case shuffled:
                return shuffledSongs.get(index);
            case alphabetic:
                return alphabeticallySortedSongs.get(index);
        }
        
        return null; // Should never happen
    }
    
    public void setMode(PlaylistMode mode) {
        this.mode = mode;
    }
    
    public PlaylistMode getMode() {
        return mode;
    }
    
    // MARK: - Private methods
    
    private void randomInsertSong(Song song) {
        if(shuffledSongs.size() == 0) {
            shuffledSongs.add(song);
            return;
        } else {
            Random random = new Random();
            int index = random.nextInt(shuffledSongs.size() + 1);
            shuffledSongs.add(index, song);
        }
    }
    
    private void alphabeticallyInsertSong(Song song) {
        for(int i = 0 ; i < alphabeticallySortedSongs.size(); i++) {
            Song sortedSong = alphabeticallySortedSongs.get(i);
            if(song.getSongName().compareTo(sortedSong.getSongName()) < 0) {
                alphabeticallySortedSongs.add(i, song);
                return;
            }
        }
        
        alphabeticallySortedSongs.add(song);
    }
    
}
