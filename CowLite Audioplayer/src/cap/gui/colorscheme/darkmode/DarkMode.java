/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme.darkmode;

import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.ControlImageSet;
import cap.gui.colorscheme.GUIImageSet;
import cap.gui.colorscheme.MenuColorScheme;
import cap.gui.colorscheme.PlaylistPaneColorScheme;
import cap.gui.colorscheme.SavedListsPaneColorScheme;
import cap.gui.colorscheme.SliderColorScheme;
import java.awt.Color;
import java.io.IOException;

/**
 *
 * @author Wessel
 */
public class DarkMode implements ColorScheme {
    
    private static DarkModeImageSet imageSet;
    
    public DarkMode() throws IOException {
        if(imageSet == null) {
            imageSet =  new DarkModeImageSet();
        }
    }

    @Override
    public Color frameColor() {
        return new Color(0x333333);
    }

    @Override
    public GUIImageSet imageSet() {
        return imageSet;
    }

    @Override
    public MenuColorScheme menu() {
        return new MenuColorScheme() {
            @Override
            public Color menuBarTextColor() {
                return new Color(0x909090);
            }

            @Override
            public Color menuBarBorderColor() {
                return new Color(0x909090);
            }

            @Override
            public Color menuTextColor() {
                return new Color(0x333333);
            }

            @Override
            public Color menuBackgroundColor() {
                return new Color(0x909090);
            }
        };
    }

    @Override
    public PlaylistPaneColorScheme playlist() {
        return new PlaylistPaneColorScheme() {
            @Override
            public Color firstBackgroundColor() {
                return new Color(0x909090);
            }

            @Override
            public Color secondBackgroundColor() {
                return new Color(0xA2A2A2);
            }

            @Override
            public Color textColor() {
                return new Color(0x333333);
            }

            @Override
            public Color highlightBackgroundColor() {
                return new Color(0xb00012);
            }

            @Override
            public Color highlightTextColor() {
                return Color.white;
            }
        };
    }

    @Override
    public SavedListsPaneColorScheme savedLists() {
        return new SavedListsPaneColorScheme() {
            @Override
            public Color backgroundColor() {
                return new Color(0x909090);
            }

            @Override
            public Color textColor() {
                return new Color(0x333333);
            }

            @Override
            public Color highlightBackgroundColor() {
                return new Color(0xb00012);
            }

            @Override
            public Color highlightTextColor() {
                return Color.white;
            }
        };
    }

    @Override
    public SliderColorScheme timeSliderColor() {
        return new SliderColorScheme() {
            @Override
            public Color backgroundColor() {
                return new Color(0x909090);
            }

            @Override
            public Color fillColor() {
                return new Color(0xb00012);
            }
        };
    }

    @Override
    public SliderColorScheme volumeColor() {
        return new SliderColorScheme() {
            @Override
            public Color backgroundColor() {
                return new Color(0x909090);
            }

            @Override
            public Color fillColor() {
                return new Color(0xb00012);
            }
        };
    }

    @Override
    public Color scrollBar() {
        return new Color(0x333333);
    }
    
}
