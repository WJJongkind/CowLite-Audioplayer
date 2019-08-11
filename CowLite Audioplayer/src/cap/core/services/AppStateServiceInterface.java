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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wessel
 */
public interface AppStateServiceInterface {
    public void saveControls(Map<HotkeyListener.Control, String> controls);
    public void saveVolume(double volume);
    public void savePlaylistMode(PlaylistMode mode);
    public void saveWindowSettings(Point location, Dimension size, boolean isFullScreen);
    public void saveOverlaySettings(Point location, Dimension size, boolean isEnabled);
    public Map<HotkeyListener.Control, String> getControls();
    public double getVolume();
    public PlaylistMode getPlaylistMode();
    public Point getWindowLocation();
    public Dimension getWindowSize();
    public Point getOverlayLocation();
    public Dimension getOverlaySize();
    public boolean isWindowFullScreen();
    public boolean isOverlayEnabled();
}
