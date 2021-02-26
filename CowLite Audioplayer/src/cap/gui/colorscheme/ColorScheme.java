/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

/**
 * Interface which defines what a ColorScheme should look like at minimum.
 * @author Wessel Jongkind
 */
public interface ColorScheme {
    
    /**
     * The font that is used in the application.
     * @return The font that is used in the application.
     */
    public DynamicFont font(); // TODO for accessibility, make observer structure where if font-scale is increased observers are notified.
    
    /**
     * The colorscheme for basic UI elements such as the window's frame and standard backgrounds etc.
     * @return 
     */
    public GeneralColorScheme general();
    
    /**
     * The colorscheme that buttons should use.
     * @return 
     */
    public ButtonColorScheme defaultButton();
    
    /**
     * The colorscheme that inputfields should use.
     * @return 
     */
    public InputFieldColorScheme defaultInputField();
    
    /**
     * The colorscheme that menus should use.
     * @return 
     */
    public MenuColorScheme menu();
    
    /**
     * The colorscheme that playlist views should use.
     * @return 
     */
    public TableColorScheme playlist();
    
    /**
     * The colorscheme that SavedLists views should use.
     * @return 
     */
    public SavedListsPaneColorScheme savedLists();
    
    /**
     * The colorscheme that timesliders should use.
     * @return 
     */
    public SliderColorScheme timeSlider();
    
    /**
     * The colorscheme that volumesliders should use.
     * @return 
     */
    public SliderColorScheme volumeSlider();
    
    /**
     * The colorscheme that overlays should use.
     * @return 
     */
    public OverlayColorScheme overlay();
    
    /**
     * The imageset for the application.
     * @return 
     */
    public UIImageSet imageSet();
    
    /**
     * Returns a perfect copy of the ColorScheme. All properties are also copied so that no references are left to properties of the copied ColorScheme's properties.
     * @return 
     */
    public ColorScheme copy();
    
}
