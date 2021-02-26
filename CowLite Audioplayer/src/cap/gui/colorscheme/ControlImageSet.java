/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.image.BufferedImage;

/**
 * This class contains all three images required to represent possible states for a imaged button:
 * 1) Enabled and not pressed
 * 2) Enabled and pressed
 * 3) disabled
 * @author Wessel Jongkind
 */
public class ControlImageSet {
    
    // MARK: - Private properties
    
    private final BufferedImage defaultImage, pressedImage, disabledImage;
    
    // MARK: - Initialisers
    
    /**
     * Initialises a new ControlImageSet.
     * @param defaultImage The image for when the control is enabled and not pressed.
     * @param pressedImage The image for when the control is enabled and pressed.
     * @param disabledImage The image for when the control is disabled.
     */
    public ControlImageSet(BufferedImage defaultImage, BufferedImage pressedImage, BufferedImage disabledImage) {
        this.defaultImage = defaultImage;
        this.pressedImage = pressedImage;
        this.disabledImage = disabledImage;
    }
    
    // MARK: - Public methods
    
    /**
     * 
     * @return The image for when the control is enabled and not pressed.
     */
    public BufferedImage defaultImage() {
        return defaultImage;
    }
    
    /**
     * 
     * @return The image for when the control is enabled and pressed.
     */
    public BufferedImage pressedImage() {
        return pressedImage;
    }
    
    /**
     * 
     * @return The image for when the control is disabled.
     */
    public BufferedImage disabledImage() {
        return disabledImage;
    }
}
