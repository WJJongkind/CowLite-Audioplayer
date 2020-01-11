/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * This class contains all the general colorscheme properties.
 * @author Wessel Jongkind
 */
public class GeneralColorScheme extends ColorSchemeItem<GeneralColorScheme> {
    
    // MARK: - Private properties
    
    private Color frameColor;
    private Color contentColor;
    
    // MARK: - Initialisers

    /**
     * Initialises a new GeneralColorScheme.
     * @param frameColor The color that the frame of a window should have. Components can use this to blend in with the window.
     * @param contentColor The color that a component's background should by default have, used to distinguish itself from the window.
     */
    public GeneralColorScheme(Color frameColor, Color contentColor) {
        this.frameColor = frameColor;
        this.contentColor = contentColor;
    }
    
    // MARK: - Public methods

    /**
     * Returns the frame color (The color that the frame of a window should have. Components can use this to blend in with the window).
     * @return The frame color.
     */
    public Color getFrameColor() {
        return frameColor;
    }

    /**
     * Sets the frame color (The color that the frame of a window should have. Components can use this to blend in with the window).
     * @param frameColor New color.
     */
    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
        notifyObservers();
    }

    /**
     * Returns the content color (The color that a component's background should by default have, used to distinguish itself from the window).
     * @return The content color.
     */
    public Color getContentColor() {
        return contentColor;
    }

    /**
     * Sets the content color (The color that a component's background should by default have, used to distinguish itself from the window).
     * @param contentColor New color.
     */
    public void setContentColor(Color contentColor) {
        this.contentColor = contentColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem
    
    @Override
    protected GeneralColorScheme copy() {
        return new GeneralColorScheme(frameColor, contentColor);
    }
    
}
