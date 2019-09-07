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
    
    // MARK: - Initialisers
    
    public LayoutScreen(ColorScheme colorScheme) {
        generalPane = new GeneralLayoutSettingsPane();
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
        
        super.setBackground(colorScheme.frameColor());
        super.setForeground(colorScheme.defaultContentColor());
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
            super.highlight = colorScheme.defaultContentColor();
            super.lightHighlight = colorScheme.defaultContentColor();
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
                tabPane.setBackgroundAt(tabIndex, colorScheme.defaultContentColor());
                tabPane.setForegroundAt(tabIndex, colorScheme.frameColor());
            } else {
                tabPane.setBackgroundAt(tabIndex, colorScheme.frameColor());
                tabPane.setForegroundAt(tabIndex, colorScheme.defaultContentColor());
            }
            super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, false);
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static final void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new DarkMode().frameColor());
        
        frame.add(new LayoutScreen(new DarkMode()));
        
        frame.setVisible(true);
    }
    
    
    
}
