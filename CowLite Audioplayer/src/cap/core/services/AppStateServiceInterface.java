/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist.PlaylistMode;
import cap.control.HotkeyListener;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

/**
 * Interface for services that can store app-wise state that needs to remain persistent between sessions.
 * @author Wessel Jongkind
 */
public interface AppStateServiceInterface {
    
    /**
     * Persists the given hotkey mapping.
     * @param controls The hotkeys that need to be persisted.
     */
    public void saveControls(Map<HotkeyListener.Control, String> controls);
    
    /**
     * Saves the given volume as the new default volume.
     * @param volume The volume that needs to be persisted.
     */
    public void saveVolume(double volume);
    
    /**
     * Saves the given playlist mode as the default mode.
     * @param mode The mode that needs to be persisted.
     */
    public void savePlaylistMode(PlaylistMode mode);
    
    /**
     * Saves the settings for the application's main window (size & location) & stores this for the next session.
     * @param location The location of the app's main window.
     * @param size The size of the app's main window (when not fullscreen).
     * @param isFullScreen If the window is fullscreen.
     */
    public void saveWindowSettings(Point location, Dimension size, boolean isFullScreen);
    
    /**
     * Saves the settings for the application's overlay & stores this for the next session.
     * @param location The location of the overlay.
     * @param size The size of the overlay.
     * @param isEnabled If the overlay is enabled.
     */
    public void saveOverlaySettings(Point location, Dimension size, boolean isEnabled);
    
    /**
     * Returns the persisted hotkey mapping.
     * @return The persisted hotkey mapping.
     */
    public Map<HotkeyListener.Control, String> getControls();
    
    /**
     * Returns the persisted default volume.
     * @return The persisted default volume.
     */
    public double getVolume();
    
    /**
     * Returns the persisted default playlist mode.
     * @return The persisted default playlist mode.
     */
    public PlaylistMode getPlaylistMode();
    
    /**
     * Returns the persisted window location.
     * @return The persisted window location.
     */
    public Point getWindowLocation();
    
    /**
     * Returns the persisted window size.
     * @return The persisted window size.
     */
    public Dimension getWindowSize();
    
    /**
     * Returns the persisted overlay location.
     * @return The persisted overlay location.
     */
    public Point getOverlayLocation();
    
    /**
     * Returns the persisted overlay size.
     * @return The persisted overlay size.
     */
    public Dimension getOverlaySize();
    
    /**
     * Returns if the main window should by default be fullscreen.
     * @return If the main window should by default be fullscreen.
     */
    public boolean isWindowFullScreen();
    
    /**
     * Returns if the overlay should by default be enabled.
     * @return If the overlay should by default be enabled.
     */
    public boolean isOverlayEnabled();
    
}
