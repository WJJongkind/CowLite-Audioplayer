/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import cap.audio.Playlist.PlaylistMode;
import cap.control.HotkeyListener;
import filedatareader.FileDataReader;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wessel
 */
public class AppStateService implements AppStateServiceInterface {

    // MARK: - Associated types & constants
    
    private static class Constants {
        
        // MARK: - General constants
        
        public static final String cfgDelimiter = " = ";
        public static final String valueDelimiter = ",";
        
        // MARK: - Audio keys
        
        private static final String volumeKey = "volume";
        private static final String playbackModeKey = "playback mode";
        
        // MARK: - GUI settings keys
        
        private static final String windowLocationKey = "window location";
        private static final String windowSizeKey = "window size";
        private static final String windowFullscreenKey = "window fullscreen";
        private static final String overlayLocationKey = "overlay location";
        private static final String overlaySizeKey = "overlay size";
        
        // MARK: - Config files
        
        public static File audioSettingsFile = new File("resources" + File.separatorChar + "persistence" + File.separatorChar + "audio.cfg");
        public static File controlsSettingsFile = new File("resources" + File.separatorChar + "persistence" + File.separatorChar + "controls.cfg");
        public static File graphicsSettingsFile = new File("resources" + File.separatorChar + "persistence" + File.separatorChar + "graphics.cfg");
        
    }
    
    // MARK: - Private properties
    
    private Map<HotkeyListener.Control, String> controls;
    private double volume;
    private PlaylistMode playlistMode;
    private Point windowLocation;
    private Dimension windowSize;
    private Point overlayLocation;
    private Dimension overlaySize;
    private boolean isWindowFullscreen;
    
    // MARK: - Initialisers
    
    public AppStateService(HashMap<HotkeyListener.Control, String> defaultControls, 
                           double defaultVolume, 
                           PlaylistMode defaultPlaylistMode, 
                           Dimension defaultWindowSize, 
                           Point defaultWindowLocation, 
                           Dimension defaultOverlaySize, 
                           Point defaultOverlayLocation, 
                           boolean isWindowDefaultFullscreen) {
        // Set defaults, which may be overridden by stored settings
        controls = defaultControls;
        volume = defaultVolume;
        playlistMode = defaultPlaylistMode;
        windowSize = defaultWindowSize;
        windowLocation = defaultWindowLocation;
        isWindowFullscreen = isWindowDefaultFullscreen;
        overlayLocation = defaultOverlayLocation;
        overlaySize = defaultOverlaySize;
        
        // Load stored settings
        loadControls();
        loadAudioSettings();
        loadGraphicsSettings();
    }
    
    private void loadControls() {
        try {
            List<KeyValuePair> storedHotkeys = readKeyValuePairs(Constants.controlsSettingsFile);

            HashMap<HotkeyListener.Control, String> controls = new HashMap<>();
            for(KeyValuePair keyValuePair : storedHotkeys) {
                HotkeyListener.Control control = HotkeyListener.Control.lookup.get(keyValuePair.key);
                controls.put(control, keyValuePair.value);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadAudioSettings() {
        try {
            List<KeyValuePair> storedAudioSettings = readKeyValuePairs(Constants.audioSettingsFile);
            
            for(KeyValuePair keyValuePair : storedAudioSettings) {
                switch(keyValuePair.key) {
                    case Constants.volumeKey:
                        volume = Double.parseDouble(keyValuePair.value);
                        break;
                    case Constants.playbackModeKey:
                        playlistMode = PlaylistMode.lookup.get(keyValuePair.value);
                        break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadGraphicsSettings() {
        try {
            List<KeyValuePair> storedGraphicsSettings = readKeyValuePairs(Constants.graphicsSettingsFile);
            
            for(KeyValuePair keyValuePair : storedGraphicsSettings) {
                String[] splitValue = keyValuePair.value.split(",");
                switch(keyValuePair.key) {
                    case Constants.windowSizeKey:
                        windowSize = new Dimension(Integer.parseInt(splitValue[0]), Integer.parseInt(splitValue[1]));
                        break;
                    case Constants.windowLocationKey:
                        windowLocation = new Point(Integer.parseInt(splitValue[0]), Integer.parseInt(splitValue[1]));
                        break;
                    case Constants.windowFullscreenKey:
                        isWindowFullscreen = Boolean.parseBoolean(splitValue[0]);
                        break;
                    case Constants.overlayLocationKey:
                        overlayLocation = new Point(Integer.parseInt(splitValue[0]), Integer.parseInt(splitValue[1]));
                        break;
                    case Constants.overlaySizeKey:
                        overlaySize = new Dimension(Integer.parseInt(splitValue[0]), Integer.parseInt(splitValue[1]));
                        break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // MARK: - AppStateServiceInterface

    @Override
    public void saveControls(Map<HotkeyListener.Control, String> controls) {
        this.controls = controls;
        
        SaveJob printJob = out -> {
            for(HashMap.Entry<HotkeyListener.Control, String> entry : controls.entrySet()) {
                out.println(entry.getKey().rawValue + Constants.cfgDelimiter + entry.getValue());
            }
        };
        
        try {
            performSaveJob(printJob, Constants.controlsSettingsFile);
        } catch (IOException ex) {
            // TODO not sure what to show for userfeedback here... revisit later
            ex.printStackTrace();
        }
    }

    @Override
    public void saveVolume(double volume) {
        this.volume = volume;
        
        SaveJob printJob = out -> {
            out.println(Constants.volumeKey + Constants.cfgDelimiter + volume);
            out.println(Constants.playbackModeKey + Constants.cfgDelimiter + getPlaylistMode());
        };
        
        try {
            performSaveJob(printJob, Constants.audioSettingsFile);
        } catch (IOException ex) {
            // TODO not sure what to show for userfeedback here... revisit later
            ex.printStackTrace();
        }
    }

    @Override
    public void savePlaylistMode(PlaylistMode mode) {
        this.playlistMode = mode;
        
        SaveJob printJob = out -> {
            out.println(Constants.volumeKey + Constants.cfgDelimiter + getVolume());
            out.println(Constants.playbackModeKey + Constants.cfgDelimiter + mode.rawValue);
        };
        
        try {
            performSaveJob(printJob, Constants.audioSettingsFile);
        } catch (IOException ex) {
            // TODO not sure what to show for userfeedback here... revisit later
            ex.printStackTrace();
        }
    }

    @Override
    public void saveWindowSettings(Point location, Dimension size, boolean isFullScreen) {
        this.windowLocation = location;
        this.windowSize = size;
        
        saveGraphicsSettings(location, size, isFullScreen, overlayLocation, overlaySize);
    }

    @Override
    public void saveOverlaySettings(Point location, Dimension size) {
        this.overlayLocation = location;
        this.overlaySize = size;
        
        saveGraphicsSettings(windowLocation, windowSize, isWindowFullscreen, location, size);
    }
    
    private void saveGraphicsSettings(Point windowLocation, Dimension windowSize, boolean isWindowFullScreen, Point overlayLocation, Dimension overlaySize) {
        SaveJob printJob = out -> {
            out.println(Constants.windowLocationKey + Constants.cfgDelimiter + windowLocation.x + Constants.valueDelimiter + windowLocation.y);
            out.println(Constants.windowSizeKey + Constants.cfgDelimiter + windowSize.width + Constants.valueDelimiter + windowSize.height);
            out.println(Constants.windowFullscreenKey + Constants.cfgDelimiter + isWindowFullScreen);
            out.println(Constants.overlayLocationKey + Constants.cfgDelimiter + overlayLocation.x + Constants.valueDelimiter + overlayLocation.y);
            out.println(Constants.overlaySizeKey + Constants.cfgDelimiter + overlaySize.width + Constants.valueDelimiter + overlaySize.height);
        };
        
        try {
            performSaveJob(printJob, Constants.graphicsSettingsFile);
        } catch (IOException ex) {
            // TODO not sure what to show for userfeedback here... revisit later
            ex.printStackTrace();
        }
    }
    
    @Override
    public Map<HotkeyListener.Control, String> getControls() {
        return controls;
    }
    

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public Playlist.PlaylistMode getPlaylistMode() {
        return playlistMode;
    }

    @Override
    public Point getWindowLocation() {
        return windowLocation;
    }

    @Override
    public Dimension getWindowSize() {
        return windowSize;
    }

    @Override
    public Point getOverlayLocation() {
        return overlayLocation;
    }

    @Override
    public Dimension getOverlaySize() {
        return overlaySize;
    }
    
    @Override
    public boolean isWindowFullScreen() {
        return isWindowFullscreen;
    }
    
    // MARK: - Private functions
    
    private void performSaveJob(SaveJob printJob, File destination) throws IOException {
        PrintWriter out = null;
        try {
            if(destination.exists()) {
                destination.delete();
                destination.createNewFile();
            } else {
                destination.getParentFile().mkdirs();
                destination.createNewFile();
            }
            
            out = new PrintWriter(new PrintStream(destination));
            
            printJob.perform(out);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }
    
    private List<KeyValuePair> readKeyValuePairs(File source) throws IOException {
        FileDataReader reader = new FileDataReader();
        reader.setPath(source);

        ArrayList<KeyValuePair> keyValuePairs = new ArrayList<>();
        for(String line : reader.getDataStringLines()) {
            String[] splitLine = line.split(Constants.cfgDelimiter);
            keyValuePairs.add(new KeyValuePair(splitLine[0], splitLine[1]));
        }

        return keyValuePairs;
    }
    
    // MARK: - Private associated types
    
    private interface SaveJob {
        
        public void perform(PrintWriter out);
        
    }
    
    private class KeyValuePair {
        
        public String key;
        public String value;
        
        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
        
    }
    
}
