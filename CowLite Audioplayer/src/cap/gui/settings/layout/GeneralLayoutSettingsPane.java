/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.gui.DefaultWindow;
import cap.gui.colorscheme.ColorScheme;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public class GeneralLayoutSettingsPane extends JPanel {
    
    // MARK: - Associated types & constants
    
    public interface Delegate {
        
    }
    
    // MARK: - Private properties

    private final UICustomizationComponent contentComponent;
    private final UICustomizationComponent frameComponent;
    private final DefaultWindow previewWindow;
    // private final SomeTextAdjustmentComponent textAdjustment;
    
    // MARK: - Initialisers
    
    public GeneralLayoutSettingsPane(ColorScheme colorScheme) {
        contentComponent = new UICustomizationComponent(colorScheme, "Primary content / text color: ", colorScheme.general().getContentColor());
        frameComponent = new UICustomizationComponent(colorScheme, "Window background color: ", colorScheme.general().getFrameColor());
        previewWindow = new DefaultWindow(colorScheme);
        
        super.setBackground(colorScheme.general().getFrameColor());
    }
    
    
}
