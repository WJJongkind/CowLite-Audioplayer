/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme.darkmode;

import cap.gui.colorscheme.ButtonColorScheme;
import cap.gui.colorscheme.MenuColorScheme;
import cap.gui.colorscheme.PlaylistPaneColorScheme;
import cap.gui.colorscheme.SavedListsPaneColorScheme;
import cap.gui.colorscheme.SliderColorScheme;
import java.awt.Color;
import java.io.IOException;
import cap.gui.colorscheme.UIImageSet;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.DynamicFont;
import cap.gui.colorscheme.InputFieldColorScheme;
import cap.gui.colorscheme.OverlayColorScheme;
import java.awt.Font;

/**
 *
 * @author Wessel
 */
public class DarkMode implements ColorScheme {
    
    private static DarkModeImageSet imageSet;
    private static DynamicFont font;
    
    public DarkMode() throws IOException {
        if(imageSet == null) {
            imageSet =  new DarkModeImageSet();
        }
        font = new DynamicFont(new Font("Dialog", Font.PLAIN, 11));
    }

    @Override
    public Color frameColor() {
        return new Color(0x333333);
    }

    @Override
    public Color defaultContentColor() {
        return new Color(0x909090);
    }

    @Override
    public UIImageSet imageSet() {
        return imageSet;
    }

    @Override
    public MenuColorScheme menu() {
        return new MenuColorScheme() {
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
    public ButtonColorScheme defaultButtonColorScheme() {
        return new ButtonColorScheme() {
            @Override
            public Color pressedBackgroundColor() {
                return new Color(0x707070);
            }

            @Override
            public Color backgroundColor() {
                return new Color(0x909090);
            }

            @Override
            public Color textColor() {
                return new Color(0x333333);
            }
        };
    }
    
    @Override
    public OverlayColorScheme overlay() {
        return new OverlayColorScheme() {
            @Override
            public Color backgroundColor() {
                return new Color(0, 0, 0, 0.3f);
            }

            @Override
            public Color foregroundColor() {
                return Color.white;
            }
        };
    }

    @Override
    public DynamicFont font() {
        return font;
    }

    @Override
    public InputFieldColorScheme defaultInputFieldColorScheme() {
        return new InputFieldColorScheme() {
            @Override
            public Color backgroundColor() {
                return new Color(0x333333);
            }

            @Override
            public Color textColor() {
                return new Color(0x909090);
            }

            @Override
            public Color borderColor() {
                return new Color(0x909090);
            }
        };
    }
    
}
