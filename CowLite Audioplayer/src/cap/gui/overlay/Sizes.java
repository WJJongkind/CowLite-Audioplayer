/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

/**
 *
 * @author Wessel
 */
public class Sizes
{
    private int width, primaryFont, secondaryFont;
    
    public Sizes(int width, int primaryFont, int secondaryFont)
    {
        this.width = width;
        this.primaryFont = primaryFont;
        this.secondaryFont = secondaryFont;
    }
    
    public int getPrimaryFont()
    {
        return primaryFont;
    }
    
    public int getSecondaryFont()
    {
        return secondaryFont;
    }
    
    public int getWidth()
    {
        return width;
    }
}
