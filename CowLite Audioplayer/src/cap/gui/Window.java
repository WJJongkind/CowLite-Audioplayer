/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.shared.SubMenu;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;

/**
 *
 * @author Wessel
 */
public interface Window {
    
    // MARK: - Associated types
    
    public interface WindowDelegate {
        public void didPressCloseWindow(Window window);
    }
    
    // MARK: - Interface
    
    public void presentViewController(ViewController viewController);
    public void pushViewController(ViewController viewController);
    public void popViewController();
    public void setSubMenus(SubMenu... subMenus);
    public void setDelegate(WindowDelegate delegate);
    public void setVisible(boolean visible);
    public void setSize(Dimension size);
    public Dimension getSize();
    public void setFullScreen(boolean isFullScreen);
    public boolean isFullScreen();
    public void setLocation(Point location);
    public Point getLocation();
    
}
