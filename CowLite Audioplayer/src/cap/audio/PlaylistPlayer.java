/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio;

import cap.audio.Playlist.PlaylistMode;
import javax.swing.Timer;

/**
 *
 * @author Wessel
 */
public class PlaylistPlayer {
    
    // MARK: - Private properties
    
    private final Timer timer;
    
    private final SongPlayer player;
    private Playlist playlist;
    private int currentSongIndex;
    
    // MARK: - Initialisers
    
    public PlaylistPlayer(DynamicSongPlayer player) {
        this.player = player;
        this.playlist = new Playlist();
        this.currentSongIndex = 0;
        this.timer = new Timer(1000, e -> checkShouldPlayNextSong());
        
        timer.start();
    }
    
    // MARK: - Mutating methods
    
    public void setPlaylist(Playlist playlist) {
        player.stop();
        this.playlist = playlist;
        if(playlist.getSongs().isEmpty()) {
            currentSongIndex = -1;
            player.setSong(null);
        } else {
            // TODO this causes lag with YouTube songs. Fix in YTAudioPlayer with some optimizations
            player.setSong(playlist.getSongs().get(0));
            currentSongIndex = 0;
        }
    }
    
    public Playlist getPlaylist() {
        return playlist;
    }
    
    public SongPlayer getPlayer() {
        return player;
    }
    
    public boolean playNextSong() {
        changeSongIndex(1);
        player.setSong(playlist.getSongs().get(currentSongIndex));
        return player.play();
    }
    
    public boolean playPreviousSong() {
        changeSongIndex(-1);
        player.setSong(playlist.getSongs().get(currentSongIndex));
        return player.play();
    }
    
    public void  playSongIfPresentInPlaylist(Song song) {
        int index = playlist.getSongs().indexOf(song);
        if(index != -1) {
            currentSongIndex = index;
            player.setSong(song);
            player.play();
        }
    }
    
    public void setIsShuffled(boolean shuffled) {
        playlist.setMode(PlaylistMode.shuffled);
    }
    
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
