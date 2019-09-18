/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.files;

import cap.audio.SongPlayer;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 *
 * @author Wessel
 */
public class FileSongPlayer implements SongPlayer<FileSong> {
    
    // MARK: - Constants
    
    private static final double defaultVolume = 0.5;
    private final ArrayList<WeakReference<SongPlayerObserver<FileSong>>> observers = new ArrayList<>();
    
    // MARK: - Private properties
    
    private MediaPlayer mediaPlayer;
    private FileSong activeSong;
    private PlayerState state = PlayerState.stopped;
    
    // MARK: - Initialisers
    
    public FileSongPlayer() {
        mediaPlayer = new MediaPlayerFactory().mediaPlayers().newMediaPlayer();
        setVolume(defaultVolume);
    }
    
    // MARK: - SongPlayer

    @Override
    public void play() {
        mediaPlayer.controls().play();
        PlayerState oldState = state;
        state = mediaPlayer.media().isValid() && mediaPlayer.status().isPlayable() ? PlayerState.playing : PlayerState.stopped;
        if(oldState != state) {
            unwrappedPerform(observers, observer -> observer.stateChanged(this, state));
        }
    }

    @Override
    public void pause() {
        if(state == PlayerState.playing) {
            mediaPlayer.controls().pause();
            state = PlayerState.paused;
            unwrappedPerform(observers, observer -> observer.stateChanged(this, state));
        }
    }

    @Override
    public void stop() {
        if(state == PlayerState.playing || state == PlayerState.paused) {
            mediaPlayer.controls().stop();
            state = PlayerState.stopped;
            unwrappedPerform(observers, observer -> observer.stateChanged(this, state));
        }
    }

    @Override
    public PlayerState getPlayerState() {
        return state;
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.audio().setVolume((int)Math.round(volume * 100));
        unwrappedPerform(observers, observer -> observer.volumeChanged(this, volume));
    }

    @Override
    public double getVolume() {
        return mediaPlayer.audio().volume() / 100.0;
    }

    @Override
    public void setSong(FileSong song) {
        stop();
        
        if(song == null) {
            double volume = getVolume();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayerFactory().mediaPlayers().newMediaPlayer();
            setVolume(volume);
        } else {
            mediaPlayer.media().prepare(song.getUrl().getProtocol() + "://" + song.getUrl().getPath());
            this.activeSong = song;
        }
        
        unwrappedPerform(observers, observer -> observer.songChanged(this, song));
    }

    @Override
    public FileSong getSong() {
        return activeSong;
    }

    @Override
    public void seek(long toPosition) {
        if(state == PlayerState.playing || state == PlayerState.paused) {
            mediaPlayer.controls().setTime(toPosition);
            unwrappedPerform(observers, observer -> observer.positionChanged(this, toPosition));
        }
    }

    @Override
    public long getPosition() {
        return state == PlayerState.playing && mediaPlayer.status().time() == -1 ? mediaPlayer.media().info().duration() : mediaPlayer.status().time();
    }

    @Override
    public long getDuration() {
        return mediaPlayer.media().info().duration();
    }
    
    @Override
    public void addObserver(SongPlayerObserver<FileSong> observer) {
        observers.add(new WeakReference<>(observer));
    }

    @Override
    public void removeObserver(SongPlayerObserver<FileSong> observer) {
        observers.remove(new WeakReference<>(observer));
    }
    
    // MARK: - ActionListener for timer
    
    private void notifyPositionChanged() {
        unwrappedPerform(observers, observer -> observer.positionChanged(this, getPosition()));
    }
    
}
