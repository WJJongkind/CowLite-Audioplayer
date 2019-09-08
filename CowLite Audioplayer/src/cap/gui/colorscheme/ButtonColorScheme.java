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
public class ButtonColorScheme extends ColorSchemeItem<ButtonColorScheme> {
    
    private Color pressedBackgroundColor;
    private Color backgroundColor;
    private Color textColor;

    public ButtonColorScheme(Color pressedBackgroundColor, Color backgroundColor, Color textColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        notifyObservers();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyObservers();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        notifyObservers();
    }
    
    
    
}
