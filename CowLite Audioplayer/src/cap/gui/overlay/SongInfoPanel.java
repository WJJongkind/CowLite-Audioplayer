/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.audio.Song;
import cap.audio.files.FileSong;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.OverlayColorScheme;
import cap.util.MillisecondsToTimestampConverter;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

/**
 *
 * @author Wessel
 */

class SongInfoPanel extends JComponent {

    // MARK: - Associated types & constants
    
    private static final class Layout {
        public static final int marginBetweenLines = 4;
        public static final int paddingTop = 4;
        public static final int paddingBottom = 4;
        public static final int paddingLeft = 4;
        public static final int paddingRight = 4;
    }
    
    // MARK: - Private properties

    private String artistSongText = "";
    private String playerStateText = "";
    private String playerPositionText = "";
    private String volumeText = "";
    private Font songInfoFont;
    private Font playerInfoFont;
    private OverlayColorScheme colorScheme;
    private boolean shouldDrawInfo = true;

    // MARK: - Initialisers

    public SongInfoPanel(ColorScheme colorScheme) {
        this.colorScheme = colorScheme.overlay();
        songInfoFont = colorScheme.font().l().bold().get();
        playerInfoFont = colorScheme.font().m().bold().get();
    }

    // MARK: - JComponent

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(colorScheme.getBackgroundColor());
        g2.fillRect(0, 0, getWidth(), getHeight());

        if(!shouldDrawInfo) {
            return;
        }
        
        g2.setColor(colorScheme.getForegroundColor());
        
        g2.setFont(songInfoFont);
        String trimmedArtistSongText = trimString(g2, artistSongText);
        g2.drawString(trimmedArtistSongText, getStringX(trimmedArtistSongText, g2), getYOffsetForFirstLine(songInfoFont, artistSongText) + Layout.paddingTop);

        g2.setFont(playerInfoFont);
        String trimmedPlayerState = trimString(g2, playerPositionText + "      " + volumeText);
        g2.drawString(trimmedPlayerState, getStringX(trimmedPlayerState, g2), getYOffsetForFirstLine(songInfoFont, artistSongText) + playerInfoFont.getSize() + Layout.marginBetweenLines + Layout.paddingTop);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, getYOffsetForFirstLine(songInfoFont, artistSongText) + playerInfoFont.getSize() + Layout.marginBetweenLines + getFontDescent(playerInfoFont, playerStateText) + Layout.paddingTop + Layout.paddingBottom);
    }

    // MARK: - Public methods
    
    public void setVolume(double volume) {
        volumeText = "Volume: " + (int)(100 * volume) + "%";
        repaint();
    }
    
    public void setTimes(long songDuration, long position) {
        playerPositionText = MillisecondsToTimestampConverter.convert(position) + " | " + MillisecondsToTimestampConverter.convert(songDuration);
        repaint();
    }
    
    public void setSong(Song song) {
        if(song == null) {
            artistSongText = "";
            repaint();
            return;
        }
        
        if(song instanceof FileSong) {
            artistSongText = song.getArtistName() + " - " + song.getSongName();
        } else {
            artistSongText = song.getSongName();
        }
        repaint();
    }
    
    public void setShouldDrawInfo(boolean shouldDrawInfo) {
        this.shouldDrawInfo = shouldDrawInfo;
    }

    // MARK: - Private methods

    private int getStringX(String str, Graphics g) {
        int stringWidth = g.getFontMetrics().stringWidth(str);
        int x = (int) Math.round(getWidth() / 2.0 - stringWidth / 2.0);

        return x;
    }

    private String trimString(Graphics g, String s) {
        String result = s;

        while(getWidth() - Layout.paddingLeft - Layout.paddingRight < g.getFontMetrics().getStringBounds(result, g).getWidth()) {
            result = result.substring(0, result.length() - 4) + "...";
        }

        return result;
    }
    
    private int getYOffsetForFirstLine(Font font, String text) {
        return font.getSize() - getFontDescent(font, text);
    }
    
    private int getFontDescent(Font font, String text) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext renderContext = new FontRenderContext(affineTransform, true, true);
        return (int) Math.round(font.getStringBounds(text, renderContext).getHeight()) - font.getSize();
    }

}