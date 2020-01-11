/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * This class contains the colorscheme for the menu.
 * @author Wessel Jongkind
 */
public class MenuColorScheme extends ColorSchemeItem<MenuColorScheme> {
    
    // MARK: - private properties
    
    private Color borderColor;
    private Color textColor;
    private Color backgroundColor;
    
    // MARK: - Initialisers

    /**
     * @param borderColor Coler of the menu's border.
     * @param textColor Color of text on the menu.
     * @param backgroundColor Color of the menu's background.
     */
    public MenuColorScheme(Color borderColor, Color textColor, Color backgroundColor) {
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }
    
    /**
     * 
     * @return The color of the menu's border.
     */
    public Color getBorderColor() {
        return borderColor;
    }
    
    /**
     * 
     * @return The color of the text on the menu.
     */
    public Color getTextColor() {
        return textColor;
    }
    
    /**
     * 
     * @return The color of the menu's background.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 
     * @param borderColor The new color.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        notifyObservers();
    }

    /**
     * 
     * @param textColor The new color.
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        notifyObservers();
    }

    /**
     * 
     * @param backgroundColor The new color.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem
    
    @Override
    protected MenuColorScheme copy() {
        return new MenuColorScheme(borderColor, textColor, backgroundColor);
    }
    
}
