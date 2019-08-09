/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.audio.Song;
import cap.audio.SongPlayer;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;

/**
 *
 * @author Wessel
 */
public class SongInfoOverlay extends JFrame {
    
    // MARK: - Constants
    
    private static final class Layout {
        public static final int maximumWidth = 380;
    }
    
    // MARK: - Private properties
    
    private final SongInfoPanel infoPanel;
    
    // MARK: - Initialisers
    
    public SongInfoOverlay(ColorScheme colorScheme) {
        this.infoPanel = new SongInfoPanel(colorScheme);
        
        super.add(infoPanel);
        super.setUndecorated(true);
        super.setBackground(new Color(0, 0, 0, 0));
        super.getContentPane().setBackground(new Color(0, 0, 0, 0));
        super.setSize(Layout.maximumWidth, infoPanel.getPreferredSize().height);
        super.setAlwaysOnTop(true);
        super.setFocusable(false);
    }
    
    public void updateForPlayer(SongPlayer player) {
        infoPanel.updateForPlayer(player);
    }
    
    public static void main(String[] args) throws IOException {
        SongInfoOverlay overlay = new SongInfoOverlay(new DarkMode());
        overlay.setLocation(1090, 0);
        overlay.updateForPlayer(new SongPlayer() {
            @Override
            public boolean play() {
                return true;
            }

            @Override
            public void pause() {
            }

            @Override
            public void stop() {
            }

            @Override
            public SongPlayer.PlayerState getPlayerState() {
                return PlayerState.playing;
            }

            @Override
            public void setVolume(double volume) {
            }

            @Override
            public double getVolume() {
                return 0.75;
            }

            @Override
            public void setSong(Song song) {
            }

            @Override
            public Song getSong() {
                return new Song() {
                    @Override
                    public String getSongName() {
                        return "Frog";
                    }

                    @Override
                    public String getAlbumName() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public String getArtistName() {
                        return "Epica";
                    }

                    @Override
                    public URL getUrl() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public long getDuration() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
            }

            @Override
            public void seek(long toPosition) {
            }

            @Override
            public long getPosition() {
                return 1000;
            }

            @Override
            public long getDuration() {
                return 300000;
            }

            @Override
            public void addObserver(SongPlayer.SongPlayerObserver observer) {
            }

            @Override
            public void removeObserver(SongPlayer.SongPlayerObserver observer) {
            }
        });
        
        
        overlay.setVisible(true);
    }
    
}
