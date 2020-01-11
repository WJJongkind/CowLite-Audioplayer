/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * This class defines the default color scheme for the overlay.
 * @author Wessel Jongkind
 */
public class OverlayColorScheme extends ColorSchemeItem<OverlayColorScheme> {
    
    // MARK: - Private properties
    
    private Color backgroundColor;
    private Color foregroundColor;
    
    // MARK: - Initialisers

    /**
     * 
     * @param backgroundColor The background color of the overlay.
     * @param foregroundColor The foreground color of the overlay.
     */
    public OverlayColorScheme(Color backgroundColor, Color foregroundColor) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }
    
    // MARK: - Public methods.

    /**
     * 
     * @return The background color for the overlay.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 
     * @param backgroundColor The new background color.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyObservers();
    }

    /**
     * 
     * @return The foreground color for the overlay.
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }

    /**
     * 
     * @param foregroundColor The new foreground color.
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem
    
    @Override
    protected OverlayColorScheme copy() {
        return new OverlayColorScheme(backgroundColor, foregroundColor);
    }
    
}
