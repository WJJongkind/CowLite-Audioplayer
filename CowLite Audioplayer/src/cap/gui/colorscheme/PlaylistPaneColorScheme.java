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
public class PlaylistPaneColorScheme {
    
    private Color firstBackgroundColor;
    private Color secondBackgroundColor;
    private Color textColor;
    private Color highlightBackgroundColor;
    private Color highlightTextColor;

    public PlaylistPaneColorScheme(Color firstBackgroundColor, Color secondBackgroundColor, Color textColor, Color highlightBackgroundColor, Color highlightTextColor) {
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
    }

    public Color getSecondBackgroundColor() {
        return secondBackgroundColor;
    }

    public void setSecondBackgroundColor(Color secondBackgroundColor) {
        this.secondBackgroundColor = secondBackgroundColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getHighlightBackgroundColor() {
        return highlightBackgroundColor;
    }

    public void setHighlightBackgroundColor(Color highlightBackgroundColor) {
        this.highlightBackgroundColor = highlightBackgroundColor;
    }

    public Color getHighlightTextColor() {
        return highlightTextColor;
    }

    public void setHighlightTextColor(Color highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
    }
    
    
}
