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
    
    public DynamicFont font(); // TODO for accessibility, make observer structure where if font-scale is increased observers are notified.
    public GeneralColorScheme general();
    public ButtonColorScheme defaultButton();
    public InputFieldColorScheme defaultInputField();
    public MenuColorScheme menu();
    public PlaylistPaneColorScheme playlist();
    public SavedListsPaneColorScheme savedLists();
    public SliderColorScheme timeSlider();
    public SliderColorScheme volumeSlider();
    public OverlayColorScheme overlay();
    public UIImageSet imageSet();
    public ColorScheme copy();
    
}
