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
public class SavedListsPaneColorScheme extends ColorSchemeItem<SavedListsPaneColorScheme> {
    
    private Color backgroundColor;
    private Color textColor;
    private Color highlightBackgroundColor;
    private Color highlightTextColor;

    public SavedListsPaneColorScheme(Color backgroundColor, Color textColor, Color highlightBackgroundColor, Color highlightTextColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.highlightBackgroundColor = highlightBackgroundColor;
        this.highlightTextColor = highlightTextColor;
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

    public Color getHighlightBackgroundColor() {
        return highlightBackgroundColor;
    }

    public void setHighlightBackgroundColor(Color highlightBackgroundColor) {
        this.highlightBackgroundColor = highlightBackgroundColor;
        notifyObservers();
    }

    public Color getHighlightTextColor() {
        return highlightTextColor;
    }

    public void setHighlightTextColor(Color highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
        notifyObservers();
    }
    
}
