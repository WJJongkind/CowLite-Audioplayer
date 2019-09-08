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
public class GeneralColorScheme extends ColorSchemeItem<GeneralColorScheme> {
    
    private Color frameColor;
    private Color contentColor;

    public GeneralColorScheme(Color frameColor, Color contentColor) {
        this.frameColor = frameColor;
        this.contentColor = contentColor;
    }

    public Color getFrameColor() {
        return frameColor;
    }

    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
        notifyObservers();
    }

    public Color getContentColor() {
        return contentColor;
    }

    public void setContentColor(Color contentColor) {
        this.contentColor = contentColor;
        notifyObservers();
    }
    
}
