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
public class TableColorScheme extends ColorSchemeItem<TableColorScheme> {
    
    private Color firstBackgroundColor;
    private Color secondBackgroundColor;
    private Color textColor;
    private Color highlightBackgroundColor;
    private Color highlightTextColor;

    public TableColorScheme(Color firstBackgroundColor, Color secondBackgroundColor, Color textColor, Color highlightBackgroundColor, Color highlightTextColor) {
        this.firstBackgroundColor = firstBackgroundColor;
        this.secondBackgroundColor = secondBackgroundColor;
        this.textColor = textColor;
        this.highlightBackgroundColor = highlightBackgroundColor;
        this.highlightTextColor = highlightTextColor;
    }

    public Color getFirstBackgroundColor() {
        return firstBackgroundColor;
    }

    public void setFirstBackgroundColor(Color firstBackgroundColor) {
        this.firstBackgroundColor = firstBackgroundColor;
        notifyObservers();
    }

    public Color getSecondBackgroundColor() {
        return secondBackgroundColor;
    }

    public void setSecondBackgroundColor(Color secondBackgroundColor) {
        this.secondBackgroundColor = secondBackgroundColor;
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
    
    // MARK: - Protected methods
    
    protected TableColorScheme copy() {
        return new TableColorScheme(firstBackgroundColor, secondBackgroundColor, textColor, highlightBackgroundColor, highlightTextColor);
    }
    
}
