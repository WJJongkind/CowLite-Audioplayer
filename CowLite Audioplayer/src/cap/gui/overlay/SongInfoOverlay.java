/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.audio.Song;
import cap.audio.SongPlayer;
import cap.gui.colorscheme.ColorScheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Wessel
 */
public class SongInfoOverlay extends JFrame implements MouseMotionListener, MouseListener, SongPlayer.SongPlayerObserver<Song> {
    
    // MARK: - Constants
    
    private static final class Layout {
        public static final int maximumWidth = 380;
    }
    
    private static final class Constants {
        public static final int refreshInterval = 500;
    }
    
    // MARK: - Private properties
    
    private final SongPlayer songPlayer;
    private final SongInfoPanel infoPanel;
    private final Timer timer;
    private boolean isMovable = false;
    private Point previousMousePoint = null;
    
    // MARK: - Initialisers
    
    public SongInfoOverlay(ColorScheme colorScheme, SongPlayer songPlayer) {
        this.songPlayer = songPlayer;
        this.infoPanel = new SongInfoPanel(colorScheme);
        infoPanel.setShouldDrawInfo(false);
        this.timer = new Timer(Constants.refreshInterval, e -> updateTrackPosition());
        
        super.add(infoPanel);
        super.setUndecorated(true);
        super.setBackground(new Color(0, 0, 0, 0));
        super.getContentPane().setBackground(new Color(0, 0, 0, 0));
        super.setSize(Layout.maximumWidth, infoPanel.getPreferredSize().height);
        super.setAlwaysOnTop(true);
        super.setFocusable(false);
        super.addMouseMotionListener(this);
        super.addMouseListener(this);
        super.setType(JFrame.Type.UTILITY);
        
        this.timer.start();
    }
    
    // MARK: - Public methods
    
    public void setIsMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }
    
    // MARK: - MouseMotionListener
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(previousMousePoint != null && isMovable) {
            int dx = e.getLocationOnScreen().x - previousMousePoint.x;
            int dy = e.getLocationOnScreen().y - previousMousePoint.y;
            int newX = getLocation().x + dx;
            int newY = getLocation().y + dy;
            super.setLocation(newX, newY);
        }
        
        previousMousePoint = e.getLocationOnScreen();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
    
    // MARK: - MouseListener
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        previousMousePoint = e.getLocationOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        previousMousePoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    // MARK: - SongPlayerObserver
    
    @Override
    public void stateChanged(SongPlayer<Song> player, SongPlayer.PlayerState state) {}

    @Override
    public void volumeChanged(SongPlayer<Song> player, double volume) {
        infoPanel.setVolume(volume);
    }

    @Override
    public void songChanged(SongPlayer<Song> player, Song song) {
        if(song == null) {
            infoPanel.setShouldDrawInfo(false);
        } else {
            infoPanel.setShouldDrawInfo(true);
            infoPanel.setSong(song);
            infoPanel.setTimes(song.getDuration(), 0);
        }
    }

    @Override
    public void positionChanged(SongPlayer<Song> player, long position) {
        infoPanel.setTimes(player.getDuration(), player.getPosition()); // TODO improve me
    }
    
    // MARK: - Timer ActionListener
    
    private void updateTrackPosition() {
        infoPanel.setTimes(songPlayer.getDuration(), songPlayer.getPosition());
    }
    
}
