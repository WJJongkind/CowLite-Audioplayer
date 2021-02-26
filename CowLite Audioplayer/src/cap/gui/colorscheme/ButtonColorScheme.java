/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * Class containing all required properties to fully lay out a basic button.
 * @author Wessel Jongkind
 */
public class ButtonColorScheme extends ColorSchemeItem<ButtonColorScheme> {
    
    // MARK: - Private properties
    
    private Color pressedBackgroundColor;
    private Color backgroundColor;
    private Color textColor;
    
    // MARK: - Initialisers

    public ButtonColorScheme(Color pressedBackgroundColor, Color backgroundColor, Color textColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }
    
    // MARK: - Public methods

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
    
    // MARK: - Protected methods
    
    @Override
    protected ButtonColorScheme copy() {
        return new ButtonColorScheme(pressedBackgroundColor, backgroundColor, textColor);
    }
    
}
