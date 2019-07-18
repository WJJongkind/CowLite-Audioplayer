/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.control.HotkeyListener;
import cap.core.audio.DynamicSongPlayer;
import cap.core.audio.PlaylistPlayer;
import cap.core.audio.SongPlayer;
import cap.core.audio.youtube.YouTubeService;
import cap.gui.Window;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.mainscreen.MainScreenController;
import java.io.IOException;
import org.jnativehook.GlobalScreen;

/**
 *
 * @author Wessel
 */
public class ApplicationCoordinator implements HotkeyListener.HotkeyListenerDelegate, Coordinator {
    
    // MARK: - Constants
    
    private static final double volumeChangeAmount = 0.05;
    
    // MARK: - Private properties
    
    private MainScreenController mainScreenController;
    private PlaylistPlayer playlistPlayer;
    
    // MARK: - Initialisers
    
    public ApplicationCoordinator(ColorScheme colorScheme, HotkeyListener hotkeyListener) throws IOException {
        playlistPlayer = new PlaylistPlayer(new DynamicSongPlayer());
        mainScreenController = new MainScreenController(colorScheme, playlistPlayer, new YouTubeService());
        
        // Catch global hotkey events
        hotkeyListener.setDelegate(this);
    }
    
    // MARK: - Coordinator
    
    @Override
    public void start(Window window) {
        window.presentViewController(mainScreenController);
    }
    
    // MARK: - HotkeyListenerDelegate
    
    @Override
    public void didPressPlay() {
        if(playlistPlayer.getPlayer().getPlayerState() == SongPlayer.PlayerState.playing) {
            playlistPlayer.getPlayer().stop();
        }
        
        if(playlistPlayer.getPlayer().getSong() == null ? playlistPlayer.playNextSong() : playlistPlayer.getPlayer().play()) {
            // TODO Make sure pause button is set
        } else {
            // TODO make sure play button is set
        }
    }

    @Override
    public void didPressPause() {
        playlistPlayer.getPlayer().pause();
        // TODO set play button
    }

    @Override
    public void didPressStop() {
        playlistPlayer.getPlayer().stop();
        // TODO set play button
    }

    @Override
    public void didPressPrevious() {
        playlistPlayer.playPreviousSong();
        // TODO update ui
    }

    @Override
    public void didPressNext() {
        playlistPlayer.playNextSong();
        // TODO update ui
    }

    @Override
    public void didPressVolumeUp() {
        playlistPlayer.getPlayer().setVolume(playlistPlayer.getPlayer().getVolume() + volumeChangeAmount);
        // TODO update ui
    }

    @Override
    public void didPressVolumeDown() {
        playlistPlayer.getPlayer().setVolume(playlistPlayer.getPlayer().getVolume() - volumeChangeAmount);
        // TODO update ui
    }

    @Override
    public void repositionOverlay(int dx, int dy) {
        // TODO
    }

    @Override
    public void allowOverlayRepositioning() {
        // TODO
    }

    @Override
    public void toggleOverlay() {
        // TODO 
    }
    
}
