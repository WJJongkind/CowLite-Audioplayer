/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;

/**
 *
 * @author Wessel
 */
public class SubMenuItem extends JMenuItem {
    
    // MARK: - Associated types
    
    public interface SubMenuAction {
        public void didSelectSubMenuItem(SubMenuItem sender);
    }
    
    // MARK: - Initialisers
    
    public SubMenuItem(String title, ColorScheme colorScheme, SubMenuAction action) {
        super(title);
        
        super.setBackground(colorScheme.menu().getBackgroundColor());
        super.setForeground(colorScheme.menu().getTextColor());
        super.setBorder(BorderFactory.createEmptyBorder());
        super.addActionListener(e -> action.didSelectSubMenuItem(this));
    }
    
}
