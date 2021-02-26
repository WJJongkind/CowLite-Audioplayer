/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * This class defines the colorscheme for the SavedListsPane.
 * @author Wessel Jongkind
 */
public class SavedListsPaneColorScheme extends ColorSchemeItem<SavedListsPaneColorScheme> {
    
    // MARK: - Private properties
    
    private Color backgroundColor;
    private Color textColor;
    private Color highlightBackgroundColor;
    private Color highlightTextColor;
    
    // MARK: - Initialisers

    /**
     * 
     * @param backgroundColor The background color for the SavedListsPane.
     * @param textColor The text color for the SavedListsPane pane.
     * @param highlightBackgroundColor The color for the selected playlist's background.
     * @param highlightTextColor  The color for the selected playlist's text.
     */
    public SavedListsPaneColorScheme(Color backgroundColor, Color textColor, Color highlightBackgroundColor, Color highlightTextColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.highlightBackgroundColor = highlightBackgroundColor;
        this.highlightTextColor = highlightTextColor;
    }
    
    // MARK: - Public methods

    /**
     * 
     * @return The background color for the SavedListsPane.
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
     * @return The text color for the SavedListsPane.
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
     * @return The selected item background color for the SavedListsPane.
     */
    public Color getHighlightBackgroundColor() {
        return highlightBackgroundColor;
    }

    /**
     * 
     * @param highlightBackgroundColor The new highlight background color.
     */
    public void setHighlightBackgroundColor(Color highlightBackgroundColor) {
        this.highlightBackgroundColor = highlightBackgroundColor;
        notifyObservers();
    }

    /**
     * 
     * @return The selected item text color for the SavedListsPane.
     */
    public Color getHighlightTextColor() {
        return highlightTextColor;
    }
    
    /**
     * 
     * @param highlightTextColor The new highlight text color.
     */
    public void setHighlightTextColor(Color highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem

    @Override
    protected SavedListsPaneColorScheme copy() {
        return new SavedListsPaneColorScheme(backgroundColor, textColor, highlightBackgroundColor, highlightTextColor);
    }
    
}
