/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.core.Coordinator;
import cap.gui.DefaultWindow;
import cap.gui.Window;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.shared.SexyColorPickerViewController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author Wessel
 */
public class LayoutSettingsCoordinator implements Coordinator, SexyColorPickerViewController.Delegate {
    
    // MARK: - Associated types & constants
    
    protected interface ColorPickerPopupCompletionHandler {
         void didSelectColor(SexyColorPickerViewController sender, Color color);
    }
    
    private static final class Constants {
        public static final Dimension popupInitialSize = new Dimension(400, 250);
    }
    
    // MARK: - Private properties
    
    private Window window;
    private final Window colorPickerPopupWindow;
    private final SexyColorPickerViewController colorPickerViewController;
    private ColorPickerPopupCompletionHandler colorPickerCompletionHandler;
            
    // MARK: - Initialisers
    
    protected LayoutSettingsCoordinator(ColorScheme colorScheme) {
        colorPickerViewController = new SexyColorPickerViewController(colorScheme);
        colorPickerViewController.setDelegate(this);
        
        colorPickerPopupWindow = new DefaultWindow(colorScheme);
        colorPickerPopupWindow.presentViewController(colorPickerViewController);
    }
    
    // MARK: - Coordinator
    
    @Override
    public void start(Window window) {
        this.window = window;
        // TODO show LayoutScreen it's viewcontroller once ready.
    }
    
    // MARK: - Private methods
    
    private void showColorPickerPopup(ColorPickerPopupCompletionHandler completionHandler) {
        this.colorPickerCompletionHandler = completionHandler;
        
        colorPickerPopupWindow.setVisible(false);
        colorPickerPopupWindow.setSize(Constants.popupInitialSize);
        colorPickerPopupWindow.setLocation(getPopupLocation());
        colorPickerPopupWindow.setVisible(true);
    }
    
    // MARK: - SexyColorPickerViewControllerDelegate
    
    @Override
    public void didSelectColor(SexyColorPickerViewController colorPicker, Color color) {
    }

    @Override
    public void didPressConfirm(SexyColorPickerViewController colorPicker, Color color) {
    }

    @Override
    public void didPressCancel(SexyColorPickerViewController colorPicker) {
    }
    
    // MARK: - Private methods
    
    private Point getPopupLocation() {
        int x = (int) Math.round(window.getLocation().x + window.getSize().width / 2.0 - colorPickerPopupWindow.getSize().width / 2.0);
        int y = (int) Math.round(window.getLocation().y + window.getSize().height / 2.0 - colorPickerPopupWindow.getSize().height / 2.0);
        return new Point(x, y);
    }
    
}