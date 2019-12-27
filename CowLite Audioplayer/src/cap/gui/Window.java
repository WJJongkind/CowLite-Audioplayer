/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.shared.SubMenu;
import java.awt.Dimension;
import java.awt.Point;

/**
 * Window describes an interface for types that can present viewcontrollers (or make their views visible).
 * The sole purpose of a window is to have a viewcontroller as content, potentially have a menu
 * and allow for resizing / minimizing and closing the window.
 * 
 * The reason that viewcontrollers can be presented, and not just views, is that in the future
 * we may want to notify a viewcontroller if it's view is being presented or if it is being removed
 * from the navigation stack. In addition, this allows the window to retain the viewcontroller
 * rather than other types having to keep a reference.
 * @author Wessel Jongkind
 */
public interface Window {
    
    // MARK: - Associated types
    
    public interface WindowDelegate {
        
        /**
         * Notifies the delegate that the user requested to close the window because
         * he/she pressed the close button.
         * @param window The window on which the even took place.
         */
        public void didPressCloseWindow(Window window);
        
    }
    
    // MARK: - Interface
    
    /**
     * Presents the given viewcontroller's view. The viewcontroller
     * will be retained by the window. All previously presented or pushed viewcontrollers
     * will be dismissed from the navigation stack.
     * @param viewController The viewcontroller of which the view has to be presented. 
     */
    public void presentViewController(ViewController viewController);
    
    /**
     * Pushes the given viewcontroller's view on top of the at that time visible view. This
     * view can be popped from the navigation stack with the popViewController() function. 
     * The viewcontroller will remain strongly referenced from the Window for as long as
     * it's view is still in the navigation stack.
     * @param viewController The viewcontroller of which the view has to be pushed on top of the
     *                       navigation stack.
     */
    public void pushViewController(ViewController viewController);
    
    /**
     * Pops the last pushed viewcontroller's view from the navigation stack and removes
     * the reference from the Window to that ViewController. If no viewcontrollers have
     * been previously pushed, or if the stack is empty, this function will have no effect.
     */
    public void popViewController();
    
    /**
     * Sets the given SubMenu objects as the menus that need to be presented on the window.
     * Any previously set menus will be removed.
     * @param subMenus The menus that need to be set.
     */
    public void setSubMenus(SubMenu... subMenus);
    
    /**
     * Sets the delegate of this window. A Window can only have one single delegate. The
     * delegate will be notified of important Window events, such as when the user requested
     * to close the window.
     * @param delegate 
     */
    public void setDelegate(WindowDelegate delegate);
    
    /**
     * Makes the window visible or invisible.
     * @param visible If true: the window becomes visible. Otherwise the window becomes invisible.
     */
    public void setVisible(boolean visible);
    
    /**
     * Sets the size of the window.
     * @param size The new size of the window.
     */
    public void setSize(Dimension size);
    
    /**
     * Returns the current size of the window.
     * @return The current size of the window.
     */
    public Dimension getSize();
    
    /**
     * Switches the window to windowed-fullscreen mode or windowed mode depending on the given boolean.
     * If the window is in fullscreen-windowed mode, it is not resizable or movable and takes up all the space
     * on a screen.
     * @param isFullScreen If true, the window will be made windowed-fullscreen. Otherwise, the window will be just windowed mode.
     */
    public void setFullScreen(boolean isFullScreen);
    
    /**
     * Returns true if the screen is in fullscreen-windowed mode, false otherwise.
     * @return True if the screen is in fullscreen-windowed mode, false otherwise.
     */
    public boolean isFullScreen();
    
    /**
     * Sets the location of the window.
     * @param location The new location of the window.
     */
    public void setLocation(Point location);
    
    /**
     * Returns the location of the window.
     * @return The location of the window.
     */
    public Point getLocation();
    
}
