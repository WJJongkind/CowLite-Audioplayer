/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.gui.DefaultWindow;
import cap.gui.colorscheme.ColorScheme;
import java.awt.Color;
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
    private final ColorScheme previewColorScheme;
    // TODO private final SomeTextAdjustmentComponent textAdjustment;
    
    // MARK: - Initialisers
    
    public GeneralLayoutSettingsPane(ColorScheme colorScheme, ColorScheme previewColorScheme) {
        this.previewColorScheme = previewColorScheme;
        
        contentComponent = new UICustomizationComponent(colorScheme, "Primary content / text color: ", colorScheme.general().getContentColor());
        contentComponent.setDelegate(e -> showColorPickerForContentColor());
        frameComponent = new UICustomizationComponent(colorScheme, "Window background color: ", colorScheme.general().getFrameColor());
        previewWindow = new DefaultWindow(previewColorScheme);
        
        super.setBackground(colorScheme.general().getFrameColor());
    }
    
    // MARK: - Private methods
    
    private void showColorPickerForContentColor() {
        // TODO show window with SexyColorPickerViewController. This should be a shared instance between the view tabs. Hide it (if it's already presented), set location to that of the pressed button and set delegate
    }
    
    private void contentColorChanged(Color c) {
        
    }
    
}
