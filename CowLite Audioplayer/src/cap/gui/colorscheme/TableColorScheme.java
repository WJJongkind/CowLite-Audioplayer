/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Color;

/**
 * This class defines the colorscheme for a table.
 * @author Wessel Jongkind
 */
public class TableColorScheme extends ColorSchemeItem<TableColorScheme> {
    
    // MARK: - Private properties
    
    private Color firstBackgroundColor;
    private Color secondBackgroundColor;
    private Color textColor;
    private Color highlightBackgroundColor;
    private Color highlightTextColor;
    
    // MARK: - Initialisers

    /**
     * 
     * @param firstBackgroundColor The background color for row 0, 2, 4, 6 etc.
     * @param secondBackgroundColor The background color for row 1, 3, 5, 7 etc.
     * @param textColor The text color.
     * @param highlightBackgroundColor The background color for the selected row.
     * @param highlightTextColor The text color for the selected row.
     */
    public TableColorScheme(Color firstBackgroundColor, Color secondBackgroundColor, Color textColor, Color highlightBackgroundColor, Color highlightTextColor) {
        this.firstBackgroundColor = firstBackgroundColor;
        this.secondBackgroundColor = secondBackgroundColor;
        this.textColor = textColor;
        this.highlightBackgroundColor = highlightBackgroundColor;
        this.highlightTextColor = highlightTextColor;
    }
    
    // MARK: - Public methods

    /**
     * 
     * @return The background color for row 0, 2, 4, 6 etc.
     */
    public Color getFirstBackgroundColor() {
        return firstBackgroundColor;
    }

    /**
     * 
     * @param firstBackgroundColor The new background color.
     */
    public void setFirstBackgroundColor(Color firstBackgroundColor) {
        this.firstBackgroundColor = firstBackgroundColor;
        notifyObservers();
    }

    /**
     * 
     * @return The background color for row 1, 3, 5, 7 etc.
     */
    public Color getSecondBackgroundColor() {
        return secondBackgroundColor;
    }

    /**
     * 
     * @param secondBackgroundColor The new background color.
     */
    public void setSecondBackgroundColor(Color secondBackgroundColor) {
        this.secondBackgroundColor = secondBackgroundColor;
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
     * @return The background color for the selected row.
     */
    public Color getHighlightBackgroundColor() {
        return highlightBackgroundColor;
    }

    /**
     * 
     * @param highlightBackgroundColor The new background color for the selected row.
     */
    public void setHighlightBackgroundColor(Color highlightBackgroundColor) {
        this.highlightBackgroundColor = highlightBackgroundColor;
        notifyObservers();
    }

    /**
     * 
     * @return The text color for the selected row.
     */
    public Color getHighlightTextColor() {
        return highlightTextColor;
    }

    /**
     * 
     * @param highlightTextColor The new text color for the selected row.
     */
    public void setHighlightTextColor(Color highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem
    
    @Override
    protected TableColorScheme copy() {
        return new TableColorScheme(firstBackgroundColor, secondBackgroundColor, textColor, highlightBackgroundColor, highlightTextColor);
    }
    
}
