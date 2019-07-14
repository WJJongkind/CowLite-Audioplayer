/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.image.BufferedImage;

/**
 *
 * @author Wessel
 */
public class ControlImageSet {
    
    private final BufferedImage defaultImage, pressedImage, disabledImage;
    
    public ControlImageSet(BufferedImage defaultImage, BufferedImage pressedImage, BufferedImage disabledImage) {
        this.defaultImage = defaultImage;
        this.pressedImage = pressedImage;
        this.disabledImage = disabledImage;
    }
    
    public BufferedImage defaultImage() {
        return defaultImage;
    }
    
    public BufferedImage pressedImage() {
        return pressedImage;
    }
    
    public BufferedImage disabledImage() {
        return disabledImage;
    }
}
