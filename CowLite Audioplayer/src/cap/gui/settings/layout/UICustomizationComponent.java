/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public class UICustomizationComponent extends JPanel {
    
    // MARK: - Associated types & constants
    
    public interface Delegate {
        public void didPressSelectColorButton();
    }
    
    private static final class Layout {
        public static final int buttonBorderWidth = 3;
    }
    
    // MARK: - Private properties
    
    private final JLabel label;
    private final JButton button;
    
    private WeakReference<Delegate> delegate;
    
    // MARK: - Initialisers
    
    public UICustomizationComponent(ColorScheme colorScheme, String labelText, Color initialColor) {
        label = new JLabel(labelText);
        label.setFont(colorScheme.font().m().get());
        label.setForeground(colorScheme.general().getContentColor());
        
        button = new JButton();
        button.setPreferredSize(new Dimension(colorScheme.font().m().getSize(), colorScheme.font().m().getSize()));
        button.setBackground(initialColor);
        button.setBorder(BorderFactory.createLineBorder(colorScheme.general().getContentColor(), Layout.buttonBorderWidth));
        button.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressSelectColorButton()));
        
        super.setBackground(new Color(0, 0, 0, 0));
        
        layoutComponents();
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - Private methods
    
    private void layoutComponents() {
        super.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        
        super.add(label, c);
        
        c.gridx = 2;
        c.anchor = GridBagConstraints.EAST;
        
        super.add(button, c);
    }
    
}
