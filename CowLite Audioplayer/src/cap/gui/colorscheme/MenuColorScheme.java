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
public class MenuColorScheme extends ColorSchemeItem<MenuColorScheme> {
    
    private Color borderColor;
    private Color textColor;
    private Color backgroundColor;

    public MenuColorScheme(Color borderColor, Color textColor, Color backgroundColor) {
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
    public Color getTextColor() {
        return textColor;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        notifyObservers();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        notifyObservers();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyObservers();
    }
    
}
