/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.about;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import cap.gui.shared.Button;
import java.awt.Insets;
import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;
import javax.swing.border.Border;

/**
 *
 * @author Wessel
 */
class AboutScreen extends JPanel {
    
    // MARK: - Associated types
    
    interface AboutScreenDelegate {
        
        void didPressClose(AboutScreen sender);
        
    }
    
    private static final class Layout {
        public static final int marginBetweenTextFieldAndButton = 8;
        public static final Border border = new EmptyBorder(8, 8, 16, 8);
    }
    
    // MARK: - Private properties
    
    private final Button button;
    
    private WeakReference<AboutScreenDelegate> delegate;
    
    // MARK: - Initialisers
    
    public AboutScreen(ColorScheme layout) {
        super.setBorder(Layout.border);
        super.setBackground(new Color(0, 0, 0, 0));
        
        AboutPanel aboutPanel = new AboutPanel(layout);
        
        super.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        
        super.add(aboutPanel, c);
        
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = c.NONE;
        c.insets = new Insets(Layout.marginBetweenTextFieldAndButton, 0, 0, 0);
        
        button = new Button("Close", layout.defaultButton(), layout.font().m().bold());
        button.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressClose(this)));
        
        super.add(button, c);
    }
    
    // MARK: - Public functions
    
    public void setDelegate(AboutScreenDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
}
