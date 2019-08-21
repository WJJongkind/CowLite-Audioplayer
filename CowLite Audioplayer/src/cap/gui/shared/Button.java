/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.ButtonColorScheme;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Wessel
 */
public class Button extends JButton {
    
    // MARK: - Private properties
    
    private ButtonColorScheme colorScheme;
    
    // MARK: - Initialisers
    
    public Button(String title, ButtonColorScheme buttonColorScheme) {
        super(title);
        
        this.colorScheme = buttonColorScheme;
        
        super.setMargin(new Insets(0, 8, 0, 8));
        super.setBackground(buttonColorScheme.backgroundColor());
        super.setForeground(buttonColorScheme.textColor());
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);
        super.setFont(super.getFont().deriveFont(Font.BOLD, 18));
    }
    
    // MARK: - JComponent
    
    @Override
    public void paintComponent(Graphics g) {
        // Draw the background
        g.setColor(getModel().isPressed() ? colorScheme.pressedBackgroundColor() : colorScheme.backgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw remainder of button.
        super.paintComponent(g);
    }
    
    // MARK: - Public methods
    
    public void removeActionListeners() {
        for(ActionListener listener : super.getActionListeners()) {
            super.removeActionListener(listener);
        }
    }
    
}
