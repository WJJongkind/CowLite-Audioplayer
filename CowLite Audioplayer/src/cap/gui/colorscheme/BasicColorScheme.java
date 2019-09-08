/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

/**
 *
 * @author Wessel
 */
public class BasicColorScheme implements ColorScheme {
    
    private DynamicFont font;
    private GeneralColorScheme generalColorScheme;
    private ButtonColorScheme defaultButtonColorScheme;
    private InputFieldColorScheme defaultInputFieldColorScheme;
    private MenuColorScheme menuColorScheme;
    private PlaylistPaneColorScheme playlistPaneColorScheme;
    private SavedListsPaneColorScheme savedListsPaneColorScheme;
    private SliderColorScheme timeSliderColorScheme;
    private SliderColorScheme volumeSliderColorScheme;
    private OverlayColorScheme overlayColorScheme;
    private UIImageSet imageSet;

    public BasicColorScheme(
            DynamicFont font, 
            GeneralColorScheme generalColorScheme,
            ButtonColorScheme defaultButtonColorScheme, 
            InputFieldColorScheme defaultInputFieldColorScheme, 
            MenuColorScheme menuColorScheme, 
            PlaylistPaneColorScheme playlistPaneColorScheme, 
            SavedListsPaneColorScheme savedListsPaneColorScheme, 
            SliderColorScheme timeSliderColorScheme, 
            SliderColorScheme volumeSliderColorScheme, 
            OverlayColorScheme overlayColorScheme, 
            UIImageSet imageSet
    ) {
        this.font = font;
        this.generalColorScheme = generalColorScheme;
        this.defaultButtonColorScheme = defaultButtonColorScheme;
        this.defaultInputFieldColorScheme = defaultInputFieldColorScheme;
        this.menuColorScheme = menuColorScheme;
        this.playlistPaneColorScheme = playlistPaneColorScheme;
        this.savedListsPaneColorScheme = savedListsPaneColorScheme;
        this.timeSliderColorScheme = timeSliderColorScheme;
        this.volumeSliderColorScheme = volumeSliderColorScheme;
        this.overlayColorScheme = overlayColorScheme;
        this.imageSet = imageSet;
    }

    @Override
    public DynamicFont font() {
        return font;
    }
    
    @Override
    public GeneralColorScheme general() {
        return generalColorScheme;
    }
    
    @Override
    public ButtonColorScheme defaultButton() {
        return defaultButtonColorScheme;
    }

    @Override
    public InputFieldColorScheme defaultInputField() {
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
    public SliderColorScheme timeSlider() {
        return timeSliderColorScheme;
    }

    @Override
    public SliderColorScheme volumeSlider() {
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

    @Override
    public ColorScheme copy() {
        return new BasicColorScheme(
                font.copy(),
                generalColorScheme.copy(),
                defaultButtonColorScheme.copy(),
                defaultInputFieldColorScheme.copy(),
                menuColorScheme.copy(),
                playlistPaneColorScheme.copy(),
                savedListsPaneColorScheme.copy(),
                timeSliderColorScheme.copy(),
                volumeSliderColorScheme.copy(),
                overlayColorScheme.copy(),
                imageSet.copy()
        );
    }
    
}
