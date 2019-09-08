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
public class OverlayColorScheme extends ColorSchemeItem<OverlayColorScheme> {
    
    private Color backgroundColor;
    private Color foregroundColor;

    public OverlayColorScheme(Color backgroundColor, Color foregroundColor) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyObservers();
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        notifyObservers();
    }
    
}
