/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.core.CowLiteAudioPlayer;
import cap.util.IO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;

/**
 *
 * @author Wessel
 */
public class InfoComponent extends JComponent
{
    private String time, volume, song;
    private int offsetX, offsetY, textIndex, alphaMultiplier;
    private Color text1, text2, actualBackgroundColor, actualText, textBG;
    private boolean drawBackground, fixedwidth, ready;
    private ArrayList<Color> textColors;
    private HashMap<String, Sizes> sizes;
    private Sizes activeSizes;
    
    //Constants
    public static final int SAFEZONE = 4, FIRST_SONG_CHAR = 4, MEDIUMWIDTH = 380, 
            SMALLWIDTH = 270, LARGEWIDTH = 500, MEDIUM_PRIMARY_FONT = 20, 
            SMALL_PRIMARY_FONT = 14, LARGE_PRIMARY_FONT = 26, 
            MEDIUM_SECONDARY_FONT = 15, SMALL_SECONDARY_FONT = 11, 
            LARGE_SECONDARY_FONT = 20;
    
    public static final String SPACER = "      ";
    
    public InfoComponent()
    {
        ready = false;
        alphaMultiplier = 1;
        fixedwidth = false;
        drawBackground = true;
        textColors = new ArrayList<>();
        text1 = Color.white;
        text2 = Color.black;
        actualBackgroundColor = new Color(0,0,0,0.3f);
        textColors.add(text1);
        textColors.add(text2);
        offsetX = 0;
        offsetY = 0;
        textIndex = -1;
        this.rotateTextColor();
        
        sizes = new HashMap<>();
        sizes.put("small", new Sizes(SMALLWIDTH, SMALL_PRIMARY_FONT, SMALL_SECONDARY_FONT));
        sizes.put("medium", new Sizes(MEDIUMWIDTH, MEDIUM_PRIMARY_FONT, MEDIUM_SECONDARY_FONT));
        sizes.put("large", new Sizes(LARGEWIDTH, LARGE_PRIMARY_FONT, LARGE_SECONDARY_FONT));
        
        try{
            BufferedReader red = new BufferedReader(new FileReader(IO.getDocumentsFolder() + "\\resources\\launchersettings\\overlay.txt"));
            activeSizes = sizes.get(red.readLine());
            red.close();
        }catch(Exception e){}
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        try{
            int stringCount = 0;
            String time_vol_combined = time + SPACER + volume;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            String font = g2.getFont().getFontName();
            
            Rectangle2D songbounds;
            Rectangle2D combinedbounds;
            stringCount++;
            Rectangle2D maximumBounds;
            
            do{
                g2.setFont(new Font(font, Font.BOLD, activeSizes.getPrimaryFont()));
                songbounds = g2.getFontMetrics().getStringBounds(song, g2);
                g2.setFont(new Font(font, Font.BOLD, activeSizes.getSecondaryFont()));
                combinedbounds = g2.getFontMetrics().getStringBounds(time_vol_combined, g2);
                maximumBounds = getMaximumBounds(songbounds, combinedbounds);
                
                if(maximumBounds.getWidth() > activeSizes.getWidth())
                    song = song.substring(0, song.length() - FIRST_SONG_CHAR) + "...";
            }while(maximumBounds.getWidth() > activeSizes.getWidth());
            
            
            g2.setColor(new Color(0,0,0,0.3f * alphaMultiplier));
            if(drawBackground)
            {
                if(fixedwidth)
                {
                    g2.fillRect(offsetX + getWidth() / 2 - activeSizes.getWidth() / 2, 
                               offsetY + stringCount * SAFEZONE / 2, 
                               activeSizes.getWidth(), 
                               activeSizes.getPrimaryFont() + activeSizes.getSecondaryFont() + stringCount * SAFEZONE);
                }
                else
                {
                    g2.fillRect(getWidth() / 2 - (int)(maximumBounds.getWidth() / 2) + offsetX - SAFEZONE / 2, 
                               offsetY + stringCount * SAFEZONE / 2, 
                               (int) maximumBounds.getWidth() + SAFEZONE, 
                               activeSizes.getSecondaryFont() + activeSizes.getPrimaryFont() + stringCount * SAFEZONE);
                }
            }
            
            g2.setFont(new Font(font, Font.BOLD, activeSizes.getPrimaryFont()));
            g2.setColor(getActualText());
            g2.drawString(song, getWidth() / 2 - (int)(songbounds.getWidth() / 2) + offsetX, activeSizes.getPrimaryFont() + offsetY);
            g2.setFont(new Font(font, Font.BOLD, activeSizes.getSecondaryFont()));
            g2.drawString(time_vol_combined, getWidth() / 2 - (int)(combinedbounds.getWidth() / 2) + offsetX, activeSizes.getSecondaryFont() + activeSizes.getPrimaryFont() + offsetY + SAFEZONE);
        
        }catch(Exception e){}
        if(alphaMultiplier == 0)
            ready = true;
        else
            ready = false;
    }
    
    public Color getActualText()
    {
        return new Color(actualText.getRed() / 255f, actualText.getGreen() / 255f, actualText.getBlue() / 255f, 1f * alphaMultiplier);
    }
    
    private Rectangle2D getMaximumBounds(Rectangle2D songbounds, Rectangle2D combinedbounds)
    {
        double songWidth = songbounds.getWidth();
        double combinedWidth = combinedbounds.getWidth();
        Rectangle rect;
        if(songWidth > combinedWidth)
            return new Rectangle((int) songWidth, (int)(songbounds.getHeight() + combinedbounds.getHeight()));
        else
            return new Rectangle((int)combinedWidth, (int)(combinedbounds.getHeight() + songbounds.getHeight()));
    }
    
    public void setSong(String song)
    {
        this.song = song;
    }
    
    public void setVolume(double volume)
    {
        this.volume = "Volume: " + (int)(100*volume) + "%";
    }
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public void setActiveSizes(String size)
    {
        activeSizes = sizes.get(size);
    }
    
    public void hide(boolean shouldBeHidden)
    {
        ready = false;
        if(shouldBeHidden)
            alphaMultiplier = 0;
        else
            alphaMultiplier = 1;
        repaint();
    }
    
    public boolean isReady()
    {
        return ready;
    }
    
    public void rotateTextColor()
    {
        textIndex++;
        if(textIndex >= textColors.size())
            textIndex = 0;
        actualText = textColors.get(textIndex);
    }
    
    public void toggleOverlayBackground()
    {
        if(!drawBackground && !fixedwidth)
        {
            drawBackground = !drawBackground;
            fixedwidth = true;
            return;
        }
        if(fixedwidth)
        {
            fixedwidth = !fixedwidth;
            return;
        }
        drawBackground = false;
    }
    
    public void setOffsetX(int x)
    {
        this.offsetX = x;
    }
    
    public void setOffsetY(int y)
    {
        this.offsetY = y;
    }
    
    public void changeOffsetX(int addX)
    {
        this.offsetX+=addX;
    }
    
    public void changeOffsetY(int addY)
    {
        this.offsetY+=addY;
    }
}
