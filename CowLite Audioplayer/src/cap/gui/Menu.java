/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.colorscheme.ColorScheme;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author Wessel
 */
public class Menu extends JPanel {
    
    private final JPopupMenu file, settings, help;
    
    public Menu(ColorScheme colorScheme) {
        super.setBackground(colorScheme.frameColor());
        super.setLayout(new FlowLayout());
        super.setBackground(Color.cyan);
        super.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        file = new JPopupMenu("File");
        file.setForeground(colorScheme.backgroundColor());
        settings = new JPopupMenu("Settings");
        settings.setForeground(colorScheme.backgroundColor());
        help = new JPopupMenu("Help");
        help.setForeground(colorScheme.backgroundColor());
        file.setBackground(Color.red);
        settings.setBackground(Color.yellow);
        
        
        
        file.setPreferredSize(new Dimension(100, 100));
        
        super.add(file);
        super.add(settings);
        super.add(help);
    }
}
