/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;
import javax.annotation.Nonnull;

/**
 *
 * @author Wessel
 */
public class InputFieldColorScheme extends ColorSchemeItem<InputFieldColorScheme> {
    
    private Color backgroundColor;
    private Color textColor;
    private Color borderColor;
    
    public InputFieldColorScheme(@Nonnull Color backgroundColor, @Nonnull Color textColor, @Nonnull Color borderColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.borderColor = borderColor;
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

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        notifyObservers();
    }
    
    // MARK: - Proteced methods
    
    protected InputFieldColorScheme copy() {
        return new InputFieldColorScheme(backgroundColor, textColor, borderColor);
    }
    
}
