package cap.gui.mainscreen;

import cap.gui.shared.Slider;
import cap.gui.mainscreen.MusicControlPane.MusicControlPaneDelegate;
import cap.gui.mainscreen.PlaylistPane.SongSelectionDelegate;
import cap.gui.mainscreen.SavedPlaylistsPane.PlayListSelectionDelegate;
import cap.gui.mainscreen.VolumeSlider.VolumeSliderDelegate;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.mainscreen.TimeSlider.TimeSliderDelegate;

/**
 * (c) Copyright
 * The main GUI. Here all buttons, labels, frame-items etc. are created
 * & added to the interface of CowLite Audio Player.
 */
public class MainScreen extends JPanel {
    
    // MARK: - Associated types & constants
    
    public interface MainScreenDelegate extends MusicControlPaneDelegate, PlayListSelectionDelegate, SongSelectionDelegate, TimeSliderDelegate, VolumeSliderDelegate {
    }
    
    private class Layout {
        
        // MARK: - Margins
        
        public static final int marginTop = 8;
        public static final int marginLeft = 8;
        public static final int marginRight = 8;
        public static final int marginBottom = 8;
        public static final int playlistMarginRight = 8;
        public static final int playlistMarginBottom = 8;
        public static final int timeSliderMarginBottom = 8;
        public static final int timeSliderMarginRight = 8;
        public static final int volumeSliderMarginBottom = 8;
        public static final int controlPaneMarginLeft = 3;
        
        // MARK: - Sliders
        
        public static final int timeSliderThickness = 5;
        public static final int volumeSliderThickness = 5;
        
        // MARK: - Saved playlists
        
        public static final int savedPlaylistsMinimumWidth = 150;
        
    }
    
    // MARK: - UI elements
    
    private final JSplitPane playlistsSplitPane;
    private final MusicControlPane controlPane;
    private final PlaylistPane playlistPane;
    private final SavedPlaylistsPane savedPlaylistsPane;
    private final VolumeSlider volumeSlider;
    private final TimeSlider timeSlider;
    
    public MainScreen(ColorScheme colorScheme) {
        controlPane = new MusicControlPane(colorScheme);
        playlistPane = new PlaylistPane<>(colorScheme);
        savedPlaylistsPane = new SavedPlaylistsPane(colorScheme);
        volumeSlider = new VolumeSlider(colorScheme);
        volumeSlider.setPreferredSize(new Dimension(Layout.volumeSliderThickness, 0));
        timeSlider = new TimeSlider(colorScheme);
        timeSlider.setPreferredSize(new Dimension(0, Layout.timeSliderThickness));
        playlistsSplitPane = new PlaylistsSplitPane(colorScheme, JSplitPane.HORIZONTAL_SPLIT, savedPlaylistsPane, playlistPane);
        playlistsSplitPane.setDividerSize(5);
        
        super.setBackground(colorScheme.general().getFrameColor());
        layoutComponents();
    }
    
    private void layoutComponents() {
        super.setLayout(new GridBagLayout());
        
        layoutPlaylistsSplitPane();
        layoutVolumeSlider();
        layoutTimeSlider();
        layoutControlPane();
    }
    
    private void layoutPlaylistsSplitPane() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.BOTH;
        c.insets = new Insets(Layout.marginTop, Layout.marginLeft, Layout.playlistMarginBottom, Layout.playlistMarginRight);
        
        savedPlaylistsPane.setMinimumSize(new Dimension(Layout.savedPlaylistsMinimumWidth, 0));
        playlistsSplitPane.setDividerLocation(Layout.savedPlaylistsMinimumWidth);
        
        add(playlistsSplitPane, c);
    }
    private void layoutVolumeSlider() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.VERTICAL;
        c.insets = new Insets(Layout.marginTop, 0, Layout.volumeSliderMarginBottom, Layout.marginRight);
        
        add(volumeSlider, c);
    }
    
    private void layoutTimeSlider() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.HORIZONTAL;
        c.insets = new Insets(0, Layout.marginLeft, Layout.timeSliderMarginBottom, Layout.timeSliderMarginRight);
        
        add(timeSlider, c);
    }
    
    private void layoutControlPane() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = c.HORIZONTAL;
        c.insets = new Insets(0, Layout.controlPaneMarginLeft, Layout.marginBottom, 0);
        
        add(controlPane, c);
    }
    
    // MARK: - Getters & Setters
    
    public void setDelegate(MainScreenDelegate delegate) {
        controlPane.setDelegate(delegate);
        playlistPane.setDelegate(delegate);
        savedPlaylistsPane.setDelegate(delegate);
        volumeSlider.setDelegate(delegate);
        timeSlider.setDelegate(delegate);
    }
    
    public PlaylistPane getPlaylistPane() {
        return playlistPane;
    }
    
    public SavedPlaylistsPane getSavedPlaylistsPane() {
        return savedPlaylistsPane;
    }
    
    public Slider getTrackPositionSlider() {
        return timeSlider;
    }
    
    public Slider getVolumeSlider() {
        return volumeSlider;
    }
    
    public MusicControlPane getControlPane() {
        return controlPane;
    }
    
}
