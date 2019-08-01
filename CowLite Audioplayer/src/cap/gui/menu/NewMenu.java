/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.menu;

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
public class NewMenu extends JMenuBar {
    
    public NewMenu(ColorScheme colorScheme) {
        super.setBackground(colorScheme.frameColor());
        super.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(colorScheme.menu().menuBarBorderColor());
                g.drawLine(x, y + height - 1, x + width, y + height - 1);
            }
        });
    }
    
    public class SubMenu extends JMenu {
        
        public SubMenu(String title, ColorScheme colorScheme) {
            super(title);
            
            super.setBorderPainted(false);
            super.getPopupMenu().setBorder(BorderFactory.createLineBorder(colorScheme.frameColor()));
            super.setForeground(colorScheme.defaultContentColor());
            super.setBackground(colorScheme.frameColor());
        }
        
    }
    
}
