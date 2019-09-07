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
public class BasicColorScheme implements ColorScheme {
    
    // TODO this smells... Maybe do go for the "Notify of transition to color scheme" method... Because this stinks.
    
    public DynamicFont font;
    public Color frameColor;
    public Color contentColor;
    public ButtonColorScheme defaultButtonColorScheme;
    public InputFieldColorScheme defaultInputFieldColorScheme;
    public MenuColorScheme menuColorScheme;
    public PlaylistPaneColorScheme playlistPaneColorScheme;
    public SavedListsPaneColorScheme savedListsPaneColorScheme;
    public SliderColorScheme timeSliderColorScheme;
    public SliderColorScheme volumeSliderColorScheme;
    public OverlayColorScheme overlayColorScheme;
    public UIImageSet imageSet;
    
    // TODO make initialiser with all properties as nonnullables

    @Override
    public DynamicFont font() {
        return font;
    }

    @Override
    public Color frameColor() {
        return frameColor;
    }

    @Override
    public Color defaultContentColor() {
        return contentColor;
    }

    @Override
    public ButtonColorScheme defaultButtonColorScheme() {
        return defaultButtonColorScheme;
    }

    @Override
    public InputFieldColorScheme defaultInputFieldColorScheme() {
        return defaultInputFieldColorScheme;
    }

    @Override
    public MenuColorScheme menu() {
        return menuColorScheme;
    }

    @Override
    public PlaylistPaneColorScheme playlist() {
        return playlistPaneColorScheme;
    }

    @Override
    public SavedListsPaneColorScheme savedLists() {
        return savedListsPaneColorScheme;
    }

    @Override
    public SliderColorScheme timeSliderColor() {
        return timeSliderColorScheme;
    }

    @Override
    public SliderColorScheme volumeColor() {
        return volumeSliderColorScheme;
    }

    @Override
    public OverlayColorScheme overlay() {
        return overlayColorScheme;
    }

    @Override
    public UIImageSet imageSet() {
        return imageSet;
    }
    
}
