/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.about;

import cap.gui.ViewController;
import javax.swing.JComponent;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class AboutViewController implements ViewController {
    
    // MARK: - Private properties
    
    private final AboutScreen aboutPanel;
    
    // MARK: - Initialisers
    
    public AboutViewController(ColorScheme colorScheme) {
        aboutPanel = new AboutScreen(colorScheme);
    }

    // MARK: - ViewController
    
    @Override
    public JComponent getView() {
        return aboutPanel;
    }
    
}
