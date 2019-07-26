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
public interface ColorScheme {
    public Color frameColor();
    public MenuColorScheme menu();
    public PlaylistPaneColorScheme playlist();
    public SavedListsPaneColorScheme savedLists();
    public SliderColorScheme timeSliderColor();
    public SliderColorScheme volumeColor();
    public Color scrollBar();
    public GUIImageSet imageSet();
}
