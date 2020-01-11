/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * This class defines the colorscheme for a slider.
 * @author Wessel Jongkind
 */
public class SliderColorScheme extends ColorSchemeItem<SliderColorScheme> {
    
    // MARK: - Private colors
    
    private Color backgroundColor;
    private Color fillColor;
    
    // MARK: - Initialisers

    /**
     * 
     * @param backgroundColor The background color for the slider.
     * @param fillColor The fill color for the slider.
     */
    public SliderColorScheme(Color backgroundColor, Color fillColor) {
        this.backgroundColor = backgroundColor;
        this.fillColor = fillColor;
    }

    // MARK: - Public methods
    
    /**
     * 
     * @return The background color for the slider.
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
     * @return The fill color for the slider.
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * 
     * @param fillColor The new fill color.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem

    @Override
    protected SliderColorScheme copy() {
        return new SliderColorScheme(backgroundColor, fillColor);
    }
    
}
