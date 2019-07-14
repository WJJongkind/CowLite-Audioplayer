/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme.darkmode;

import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.ControlImageSet;
import cap.gui.colorscheme.GUIImageSet;
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
    public Color backgroundColor() {
        return new Color(0x8E9191);
    }

    @Override
    public Color frameColor() {
        return new Color(0x333333);
    }

    @Override
    public Color textColor() {
        return new Color(0x333333);
    }

    @Override
    public GUIImageSet imageSet() {
        return imageSet;
    }

    @Override
    public Color timeSliderColor() {
        return new Color(0xb00012);
    }

    @Override
    public Color volumeColor() {
        return new Color(0xb00012);
    }
    
}
