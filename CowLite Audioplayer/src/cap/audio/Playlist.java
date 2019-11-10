/*
 * Songo change this license header, choose License Headers in Project Properties.
 * Songo change this template file, choose Songools | Songemplates
 * and open the template in the editor.
 */
package cap.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Playlist is a type which represents a playlist of songs. Aside from allowing
 * songs to be added / removed, Playlist also offers several high-level functions
 * such as alphabetically sorting the playlist and shuffling the playlist.
 * @author Wessel Jongkind
 */
public class Playlist {
    
    // MARK: - Associated types
    
    /**
     * The various "modes" which a playlist can have:
     * 
     * 1. Normal: The playlist represented as the order in which the songs were added to it.
     * 2. Shuffeld: The playlist representing the songs in a random order.
     * 3. Alphabetic: The playlist representing the songs in an alphabetic order by song name.
     */
    public enum PlaylistMode {
        
        // MARK: - Enum cases
        
        normal("normal"),
        shuffled("shuffled"),
        alphabetic("alphabetic");
        
        // MARK: - Private properties
        
        /**
         * String-representation of the enum case.
         */
        public String rawValue;
        
        // MARK: - Initialisers
        
        private PlaylistMode(String rawValue) {
            this.rawValue = rawValue;
        }
        
        // MARK: - Lookup
        
        /**
         * HashMap mapping all string-representations of the enum cases to the enum cases themselves.
         */
        public static final HashMap<String, PlaylistMode> lookup = new HashMap<>();
        
        static {
            lookup.put(normal.rawValue, normal);
            lookup.put(shuffled.rawValue, shuffled);
            lookup.put(alphabetic.rawValue, alphabetic);
        }
        
    }
    
    // MARK: - Private properties
    
    private final ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Song> shuffledSongs = new ArrayList<>();
    private final ArrayList<Song> alphabeticallySortedSongs = new ArrayList<>();
    
    private PlaylistMode mode = PlaylistMode.normal;
    private String name;
    
    // MARK: - Getters & Setters
    
    /**
     * Adds a song to this playlist.
     * @param song The song to be added.
     */
    public void addSong(Song song) {
        songs.add(song);
        randomInsertSong(song);
        alphabeticallyInsertSong(song);
    }
    
    /**
     * Adds a song to this playlist at the given index.
     * @param song The song to be added.
     * @param index The index at which it needs to be added.
     */
    public void addSong(Song song, int index) {
        
        switch(mode) {
            case normal:
                songs.add(index, song);
                randomInsertSong(song);
                alphabeticallyInsertSong(song);
                break;
            case shuffled:
                songs.add(song);
                shuffledSongs.add(index, song);
                alphabeticallyInsertSong(song);
                break;
            case alphabetic:
                // Doesn't make sense here to use index.
                songs.add(song);
                randomInsertSong(song);
                alphabeticallyInsertSong(song);
                break; 
        }
    }
    
    /**
     * Removes the given song from this playlist.
     * @param song The song that needs to be removed.
     */
    public void removeSong(Song song) {
        songs.remove(song);
        shuffledSongs.remove(song);
        alphabeticallySortedSongs.remove(song);
    }
    
    /**
     * Removes the song at the given index or throws an OutOfBoundsException if the given
     * index less than 0 or larger than the size of the array.
     * @param index The index of the song that needs to be removed.
     */
    public void removeSong(int index) {
        removeSong(getSongs().get(index));
    }
    
    /**
     * Sets the name of this playlist.
     * @param name The name that needs to be set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the name of this playlist.
     * @return The name of this playlist.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the songs in the order in which they were originally added.
     * @return The songs in original order.
     */
    public List<Song> getSongsInOriginalOrder() {
        return new ArrayList<>(songs);
    }
    
    /**
     * Returns the current representation of the playlist as a List of Song objects.
     * The current representation can either be 1. normal, 2. shuffled or 3. alphabetically sorted.
     * @return 
     */
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
    
    /**
     * Returns the song at the given index.
     * @param index The index of the song that needs  to be obtained.
     * @return The song at the given index.
     */
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
    
    /**
     * Sets the representation mode of this playlist.
     * @param mode The mode in which the playlist needs to be represented.
     */
    public void setMode(PlaylistMode mode) {
        this.mode = mode;
    }
    
    /**
     * Returns the mode in which the playlist is being represented.
     * @return The mode of the playlist.
     */
    public PlaylistMode getMode() {
        return mode;
    }
    
    // MARK: - Private methods
    
    private void randomInsertSong(Song song) {
        if(shuffledSongs.isEmpty()) {
            shuffledSongs.add(song);
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
