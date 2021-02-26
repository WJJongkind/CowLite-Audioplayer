/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.ButtonColorScheme;
import cap.gui.colorscheme.DynamicFont;
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
    
    public Button(String title, ButtonColorScheme buttonColorScheme, DynamicFont font) {
        super(title);
        
        this.colorScheme = buttonColorScheme;
        
        int margin = (int) Math.round(font.get().getSize() / 5.0);
        super.setMargin(new Insets(0, margin, 0, margin));
        super.setBackground(buttonColorScheme.getBackgroundColor());
        super.setForeground(buttonColorScheme.getTextColor());
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);
        super.setFont(font.get());
    }
    
    // MARK: - JComponent
    
    @Override
    public void paintComponent(Graphics g) {
        // Draw the background
        g.setColor(getModel().isPressed() ? colorScheme.getPressedBackgroundColor() : colorScheme.getBackgroundColor());
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
