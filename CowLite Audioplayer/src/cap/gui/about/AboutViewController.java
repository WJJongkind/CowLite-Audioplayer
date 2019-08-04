/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.about;

import cap.gui.ViewController;
import javax.swing.JComponent;
import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;

/**
 *
 * @author Wessel
 */
public class AboutViewController implements ViewController, AboutScreen.AboutScreenDelegate {
    
    // MARK: - Associated types
    
    public interface AboutViewControllerDelegate {
        
        public void didPressClose(AboutViewController sender);
        
    }
    
    // MARK: - Private properties
    
    private final AboutScreen aboutScreen;
    private WeakReference<AboutViewControllerDelegate> delegate;
    
    // MARK: - Initialisers
    
    public AboutViewController(ColorScheme colorScheme) {
        aboutScreen = new AboutScreen(colorScheme);
        aboutScreen.setDelegate(this);
    }
    
    // MARK: - Public functions
    
    public void setDelegate(AboutViewControllerDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    // MARK: - ViewController
    
    @Override
    public JComponent getView() {
        return aboutScreen;
    }
    
    // MARK: - AboutScreenDelegate
    
    @Override
    public void didPressClose(AboutScreen sender) {
        unwrappedPerform(delegate, delegate -> delegate.didPressClose(this));
    }
    
}
