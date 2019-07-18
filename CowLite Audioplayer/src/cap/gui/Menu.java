/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.colorscheme.ColorScheme;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author Wessel
 */
public class Menu extends JMenuBar {
    
    private final JMenu file, settings, help;
    
    public Menu(ColorScheme colorScheme) {
        super.setBackground(colorScheme.frameColor());
        super.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(colorScheme.menu().borderColor());
                g.drawLine(x, y + height - 1, x + width, y + height - 1);
            }
        });
        
        file = new JMenu("File");
        file.setForeground(colorScheme.menu().textColor());
        settings = new JMenu("Settings");
        settings.setForeground(colorScheme.menu().textColor());
        help = new JMenu("Help");
        help.setForeground(colorScheme.menu().textColor());
        
        super.add(file);
        super.add(settings);
        super.add(help);
    }
}
