/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio;

/**
 * A SongPlayer is a type with which specific song types can be played. It offers
 * most basic functionality such as play/pause/stop, seeking and adjusting volume.
 * 
 * @param <T> Allowed types of songs that can be played by the SongPlayer.
 * @author Wessel Jongkind
 */
public interface SongPlayer<T extends Song> {
    
    // MARK: - Associated types
    
    /**
     * Enumeration describing the various states in which a SongPlayer can be.
     */
    public enum PlayerState {
        playing,
        paused,
        stopped
    }
    
    /**
     * Protocol for types which want to observe state changes of SongPlayers. All SongPlayers
     * should notify their observers in at least the following events:
     * 
     * 1. PlayerState change
     * 2. Volume change
     * 3. Song change
     * 4. Position change after seeking
     * 
     * @param <T> The type of song equivalent to the type of song that the SongPlayer can play.
     */
    public interface SongPlayerObserver<T extends Song> {
        public void stateChanged(SongPlayer<T> player, PlayerState state);
        public void volumeChanged(SongPlayer<T> player, double volume);
        public void songChanged(SongPlayer<T> player, T song);
        public void positionChanged(SongPlayer<T> player, long position);
    }
    
    // MARK: - Interface, audio controls
    
    /**
     * Starts playing the loaded song. Has no effect if no song is loaded. Causes
     * the player's state to change to `playing` if a song is loaded and can be played.
     */
    public void play();
    
    /**
     * Pauses the player. Has no effect if the player is already paused or stopped or no song is loaded.
     * Causes the player's state to change to `paused`.
     */
    public void pause();
    
    /**
     * Stops the player and resets the song's position to 0. Has no effect if no song is loaded.
     * Causes the player's state to change to `stopped`.
     */
    public void stop();
    
    /**
     * Returns the current state of the player.
     * @return The current state of the player.
     */
    public PlayerState getPlayerState();
    
    /**
     * Sets the volume of the player.
     * @param volume The volume (between 0 and 1) of the player. 
     */
    public void setVolume(double volume);
    
    /**
     * Returns the volume of the player.
     * @return The volume of the player.
     */
    public double getVolume();
    
    /**
     * Stops the player if a song is playing, and sets a new song. State will be changed to `stopped`.
     * @param song The song that needs to be loaded.
     */
    public void setSong(T song);
    
    /**
     * Returns the currently loaded song or null.
     * @return The currently loaded song or null if no song is loaded.
     */
    public T getSong();
    
    /**
     * Seeks the player to the given position of the song that is being played. Undefined behavior
     * may occur if the value is less than 0 or greater than the actual length of the loaded song.
     * Therefore, always a check should be done prior to calling this method.
     * @param toPosition 
     */
    public void seek(long toPosition);
    
    /**
     * Returns the current position of the player.
     * @return The current position of the player.
     */
    public long getPosition();
    
    /**
     * Returns the duration of the currently loaded song.
     * @return The duration of the currently loaded song.
     */
    public long getDuration();
    
    // MARK: - Interface, others
    
    /**
     * Adds a new observer to the player which will then be notified of state changes.
     * @param observer The observer that needs to be added.
     */
    public void addObserver(SongPlayerObserver<T> observer);
    
    /**
     * Removes an observer from the player.
     * @param observer The observer that needs to be removed.
     */
    public void removeObserver(SongPlayerObserver<T> observer);
    
}
