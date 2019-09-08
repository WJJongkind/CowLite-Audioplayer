/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author Wessel
 */
public class LayoutScreen extends JTabbedPane {
    
    // MARK: - Private properties
    
    private final GeneralLayoutSettingsPane generalPane;
    private final PlaylistLayoutSettingsPane playlistPane;
    private final TrackPositionSliderLayoutSettingsPane trackPositionPane;
    private final VolumeSliderLayoutSettingsPane volumePane;
    private final MenuLayoutSettingsPane menuPane;
    private final OverlayLayoutSettingsPane overlayPane; 
    private final ColorScheme previewColorScheme;
    
    // MARK: - Initialisers
    
    public LayoutScreen(ColorScheme colorScheme) {
        previewColorScheme = colorScheme.copy();
        
        generalPane = new GeneralLayoutSettingsPane(colorScheme, previewColorScheme);
        playlistPane = new PlaylistLayoutSettingsPane();
        trackPositionPane = new TrackPositionSliderLayoutSettingsPane();
        volumePane = new VolumeSliderLayoutSettingsPane();
        menuPane = new MenuLayoutSettingsPane();
        overlayPane = new OverlayLayoutSettingsPane();
        
        super.addTab("General", generalPane);
        super.addTab("Playlist", playlistPane);
        super.addTab("Position slider", trackPositionPane);
        super.addTab("Volume slider", volumePane);
        super.addTab("Menu", menuPane);
        super.addTab("Overlay", overlayPane);
        
        super.setBackground(colorScheme.general().getFrameColor());
        super.setForeground(colorScheme.general().getContentColor());
        super.setUI(new TabbedPaneUI(colorScheme));
    }
    
    // MARK: - Private associated types
    
    private final class TabbedPaneUI extends BasicTabbedPaneUI {
        
        private final ColorScheme colorScheme;
        
        public TabbedPaneUI(ColorScheme colorScheme) {
            super();
            this.colorScheme = colorScheme;
        }
        
        @Override 
        public void installUI(JComponent c) {
            super.installUI(c);
            super.highlight = colorScheme.general().getContentColor();
            super.lightHighlight = colorScheme.general().getContentColor();
            super.shadow = new Color(0, 0, 0, 0);
            super.darkShadow = new Color(0, 0, 0, 0);
            super.focus = new Color(0, 0, 0, 0);
        }
        
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement,
                                          int tabIndex,
                                          int x, int y, int w, int h,
                                          boolean isSelected ) {
            if(isSelected) {
                tabPane.setBackgroundAt(tabIndex, colorScheme.general().getContentColor());
                tabPane.setForegroundAt(tabIndex, colorScheme.general().getFrameColor());
            } else {
                tabPane.setBackgroundAt(tabIndex, colorScheme.general().getFrameColor());
                tabPane.setForegroundAt(tabIndex, colorScheme.general().getContentColor());
            }
            super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, false);
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static final void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new DarkMode().general().getFrameColor());
        
        frame.add(new LayoutScreen(new DarkMode()));
        
        frame.setVisible(true);
    }
    
    
    
}
