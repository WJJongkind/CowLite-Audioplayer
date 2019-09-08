/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.AbstractBorder;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class Menu extends JMenuBar {
    
    public Menu(ColorScheme colorScheme) {
        super.setBackground(colorScheme.frameColor());
        super.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(colorScheme.menu().getBorderColor());
                g.drawLine(x, y + height - 1, x + width, y + height - 1);
            }
        });
    }
    
}
