/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio;

/**
 *
 * @author Wessel
 */
public interface SongPlayer<T extends Song> {
    
    // MARK: - Associated types
    
    public enum PlayerState {
        playing,
        paused,
        stopped
    }
    
    public interface SongPlayerObserver<T extends Song> {
        public void stateChanged(SongPlayer<T> player, PlayerState state);
        public void volumeChanged(SongPlayer<T> player, double volume);
        public void songChanged(SongPlayer<T> player, T song);
        public void positionChanged(SongPlayer<T> player, long position);
    }
    
    // MARK: - Interface, audio controls
    
    public boolean play();
    public void pause();
    public void stop();
    public PlayerState getPlayerState();
    public void setVolume(double volume);
    public double getVolume();
    public void setSong(T song);
    public T getSong();
    public void seek(long toPosition);
    public long getPosition();
    public long getDuration();
    
    // MARK: - Interface, others
    
    public void addObserver(SongPlayerObserver<T> observer);
    public void removeObserver(SongPlayerObserver<T> observer);
    
}
