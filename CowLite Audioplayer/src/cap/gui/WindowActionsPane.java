/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.colorscheme.ControlImageSet;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.AbstractBorder;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class WindowActionsPane extends JPanel {
    
    // MARK: - Associated types & constants
    
    public interface WindowActionsPaneDelegate {
        public void didPressCloseButton(WindowActionsPane sender);
        public void didPressMinimizeButton(WindowActionsPane sender);
        public void didPressStretchButton(WindowActionsPane sender);
    }
    
    private static class Layout {
        public static final Insets buttonInsets = new Insets(4, 0, 4, 8);
        public static final Dimension minimizeButtonSize = new Dimension(10, 10);
        public static final Dimension stretchButtonSize = new Dimension(10, 10);
        public static final Dimension closeButtonSize = new Dimension(10, 10);
    }
    
    // MARK: - Private properties
    
    private final JToggleButton minimizeButton, stretchButton, closeButton;
    private WeakReference<WindowActionsPaneDelegate> delegate;
    
    // MARK: - Initialisers
    
    public WindowActionsPane(ColorScheme colorScheme) {
        super();
        
        super.setBackground(colorScheme.general().getFrameColor());
        super.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(colorScheme.menu().getBorderColor());
                g.drawLine(x, y + height - 1, x + width, y + height - 1);
            }
        });
        
        minimizeButton = makeButton(colorScheme.imageSet().getMinimizeScreenButtonImageSet(), Layout.minimizeButtonSize);
        minimizeButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressMinimizeButton(this)));
        
        stretchButton = makeButton(colorScheme.imageSet().getStretchScreenButtonImageSet(), Layout.stretchButtonSize);
        stretchButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressStretchButton(this)));
        
        closeButton = makeButton(colorScheme.imageSet().getCloseScreenButtonImageSet(), Layout.closeButtonSize);
        closeButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressCloseButton(this)));
        
        super.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = c.BOTH;
        c.insets = Layout.buttonInsets;
        
        super.add(minimizeButton, c);
        
        c.gridx++;
        
        super.add(stretchButton, c);
        
        c.gridx++;
        
        super.add(closeButton, c);
    }
    
    private JToggleButton makeButton(ControlImageSet imageSet, Dimension size) {
        //Make the button
        JToggleButton button = new JToggleButton();
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        
        button.setIcon(new ImageIcon(imageSet.defaultImage().getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
        button.setPressedIcon(new ImageIcon(imageSet.pressedImage().getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
        button.setDisabledIcon(new ImageIcon(imageSet.disabledImage().getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
        
        return button;
    }
    
    // MARK: - Public methods
    
    public void setDelegate(WindowActionsPaneDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
}
