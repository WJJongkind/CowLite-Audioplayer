/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 *
 * @author Wessel
 */
public class SliderColorScheme extends ColorSchemeItem<SliderColorScheme> {
    
    private Color backgroundColor;
    private Color fillColor;

    public SliderColorScheme(Color backgroundColor, Color fillColor) {
        this.backgroundColor = backgroundColor;
        this.fillColor = fillColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyObservers();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        notifyObservers();
    }

    @Override
    protected SliderColorScheme copy() {
        return new SliderColorScheme(backgroundColor, fillColor);
    }
    
}
