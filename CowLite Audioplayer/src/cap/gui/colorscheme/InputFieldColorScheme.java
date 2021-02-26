/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;
import javax.annotation.Nonnull;

/**
 * ColorScheme that input fields should follow.
 * @author Wessel Jongkind
 */
public class InputFieldColorScheme extends ColorSchemeItem<InputFieldColorScheme> {
    
    // MARK: - Private properties
    
    private Color backgroundColor;
    private Color textColor;
    private Color borderColor;
    
    // MARK: - Initialisers
    
    /**
     * Initialises a new InputFieldColorScheme
     * @param backgroundColor The background for the input field.
     * @param textColor The text color of the text in the input field.
     * @param borderColor The input field's border color.
     */
    public InputFieldColorScheme(Color backgroundColor, Color textColor, Color borderColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.borderColor = borderColor;
    }
    
    // MARK: - Public methods

    /**
     * 
     * @return The background color for the input field.
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
     * @return The text color.
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * 
     * @param textColor The new text color.
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        notifyObservers();
    }

    /**
     * 
     * @return The border color.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * 
     * @param borderColor The border color.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem
    
    @Override
    protected InputFieldColorScheme copy() {
        return new InputFieldColorScheme(backgroundColor, textColor, borderColor);
    }
    
}
