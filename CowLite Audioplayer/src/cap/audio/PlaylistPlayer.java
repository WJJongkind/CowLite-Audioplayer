/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio;

import javax.swing.Timer;

/**
 * PlaylistPlayer is a type created to automatically play through playlists. If a given
 * song is finished playing, it will move on to the next song in the playlist.
 * @author Wessel Jongkind
 */
public class PlaylistPlayer {
    
    // MARK: - Private properties
    
    private final Timer timer;
    private final SongPlayer player;
    
    private Playlist playlist;
    private int currentSongIndex;
    
    // MARK: - Initialisers
    
    /**
     * Instantiates a new PlaylistPlayer by using the given DynamicSongPlayer to play back
     * the songs.
     * @param player The DynamicSongPlayer that needs to be used to play back songs.
     */
    public PlaylistPlayer(DynamicSongPlayer player) {
        this.player = player;
        this.playlist = new Playlist();
        this.currentSongIndex = 0;
        this.timer = new Timer(1000, e -> checkShouldPlayNextSong());
        
        timer.start();
    }
    
    // MARK: - Mutating methods
    
    /**
     * Stops the underlying SongPlayer and sets the playlist that needs to be played.
     * Does not actively start playing the playlist. 
     * @param playlist 
     */
    public void setPlaylist(Playlist playlist) {
        player.stop();
        this.playlist = playlist;
        if(playlist.getSongs().isEmpty()) {
            currentSongIndex = -1;
            player.setSong(null);
        } else {
            player.setSong(playlist.getSongs().get(0));
            currentSongIndex = 0;
        }
    }
    
    /**
     * Returns the playlist that is currently loaded.
     * @return The playlist that is currently loaded.
     */
    public Playlist getPlaylist() {
        return playlist;
    }
    
    /**
     * Returns the SongPlayer that is used to play songs.
     * @return The SongPlayer that is used to play songs.
     */
    public SongPlayer getPlayer() {
        return player;
    }
    
    /**
     * Starts playing the next song in the playlist, if available.
     */
    public void playNextSong() {
        changeSongIndex(1);
        player.setSong(playlist.getSongs().get(currentSongIndex));
        player.play();
    }
    
    /**
     * Starts playing the previous song in the playlist, if available.
     */
    public void playPreviousSong() {
        changeSongIndex(-1);
        player.setSong(playlist.getSongs().get(currentSongIndex));
        player.play();
    }
    
    /**
     * Starts playing the given song if it is present in the playlist that is currently
     * loaded.
     * @param song The song that needs to be played.
     */
    public void  playSongIfPresentInPlaylist(Song song) {
        int index = playlist.getSongs().indexOf(song);
        if(index != -1 && currentSongIndex != index) {
            currentSongIndex = index;
            player.setSong(song);
            player.play();
        }
    }
    
    /**
     * Loads the song into the underlying SongPlayer if it is present in the currently
     * loaded playlist.
     * @param song The song that needs to be laoded.
     */
    public void setSongIfPresentInPlaylist(Song song) {
        int index = playlist.getSongs().indexOf(song);
        if(index != -1) {
            currentSongIndex = index;
            player.setSong(song);
        }
    }
    
    /**
     * Refreshes the PlaylistPlayer by correctly setting it's internal state based on the
     * song that is currently being played by the SongPlayer. Call this method if the
     * `mode` of the loaded playlist is changed.
     */
    public void refresh() {
        Song song = player.getSong();
        
        if(song == null && playlist.getSongs().isEmpty()) {
            currentSongIndex = -1;
        } else if(song == null) {
            currentSongIndex = 0;
        } else {
            currentSongIndex = Math.max(0, playlist.getSongs().indexOf(song));
        }
        
        if(player.getSong() != playlist.getSong(currentSongIndex)) {
            player.setSong(playlist.getSong(currentSongIndex));
        }
    }
    
    // MARK: - ActionListener for Timer
    
    private void checkShouldPlayNextSong() {
        // Catch all (silent) exceptions, to ensure the timer keeps firing.
        try {
            if(playlist != null && playlist.getSongs().size() > 0 && player.getDuration() == player.getPosition() && player.getDuration() != -1) {
                playNextSong();
            }
        }catch(Exception e) {}
    }
    
    private void changeSongIndex(int amount) {
        currentSongIndex = Math.max(0, currentSongIndex + amount) % playlist.getSongs().size();
    }
    
}
