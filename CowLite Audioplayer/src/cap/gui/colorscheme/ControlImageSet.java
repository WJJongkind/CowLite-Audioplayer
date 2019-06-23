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
public interface ControlImageSet {
    public BufferedImage defaultImage();
    public BufferedImage highlightedImage();
    public BufferedImage disabledImage();
}
