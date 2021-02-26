/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.WindowActionsPane.WindowActionsPaneDelegate;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.shared.Menu;
import cap.gui.shared.SubMenu;
import java.util.Stack;

/**
 * This class represents a singular window in the application. Multiple windows can be
 * active in the application at any time. The window supports various UI operations such
 * as presenting a viewcontroller's view, or pushing new viewcontrollers to the navigation stack.
 * @author Wessel Jongkind
 */
public class DefaultWindow extends JFrame implements Window, WindowActionsPaneDelegate {
    
    // MARK: - Constants and associated types
    
    private static final Dimension minimumSize = new Dimension(400, 200);
    private static final WindowManager windowManager = new WindowManager();
    
    // MARK: - Private properties
    
    private boolean isFullScreen = false;
    private Dimension normalSize = new Dimension(0, 0);
    private Point normalLocation = new Point(0, 0);
    private WeakReference<WindowDelegate> delegate;
    
    // MARK: - UI elements
    
    private final Menu menu;
    private final JPanel contentPane;
    private final WindowActionsPane windowActionsPane;
    private final Stack<ViewController> pushedViewControllers;
    
    private ViewController presentedViewController;
    private ViewController visibleViewController;
    
    // MARK: - Initialisers
    
    /**
     * Initialises a new DefaultWindow object
     * @param colorScheme The colorscheme that needs to be applied
     */
    public DefaultWindow(ColorScheme colorScheme) {
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setBackground(colorScheme.general().getFrameColor());
        super.getContentPane().setBackground(colorScheme.general().getFrameColor());
        super.setUndecorated(true);
        super.addMouseListener(windowManager);
        super.addMouseMotionListener(windowManager);
        super.setMinimumSize(minimumSize);
        
        windowActionsPane = new WindowActionsPane(colorScheme);
        windowActionsPane.setDelegate(this);
        
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBackground(colorScheme.general().getFrameColor());
        
        menu = new Menu(colorScheme);
        
        pushedViewControllers = new Stack<>();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = c.BOTH;

        contentPane.add(menu, c);
        
        c.gridx = 2;
        c.weightx = 0;
        
        contentPane.add(windowActionsPane, c);

        super.add(contentPane);
    }
    
    // MARK: - Window

    @Override
    public void presentViewController(ViewController viewController) {
        pushedViewControllers.clear();
        this.presentedViewController = viewController;
        setVisibleViewController(viewController);
    }
    
    @Override
    public void pushViewController(ViewController viewController) {
        pushedViewControllers.push(viewController);
        setVisibleViewController(viewController);
    }
    
    @Override
    public void popViewController() {
        if(pushedViewControllers.size() <= 0) {
            return;
        }
        
        pushedViewControllers.pop();
        setVisibleViewController(pushedViewControllers.size() > 0 ? pushedViewControllers.peek() : presentedViewController);
    }
    
    private void setVisibleViewController(ViewController viewController) {
        if(visibleViewController != null) {
            contentPane.remove(visibleViewController.getView());
        }
        
        this.visibleViewController = viewController;
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = c.BOTH;
        
        contentPane.add(viewController.getView(), c);
        
        super.revalidate();
        super.repaint();
    }

    @Override
    public void setDelegate(WindowDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    @Override
    public void setSubMenus(SubMenu... subMenus) {
        for(SubMenu subMenu : subMenus) {
            menu.add(subMenu);
        }
    }
    
    @Override
    public void setFullScreen(boolean isFullScreen) {
        if(isFullScreen) {
            normalSize = getSize();
            normalLocation = getLocation();
            this.isFullScreen = true;
            super.setSize(getToolkit().getScreenSize());
            super.setLocation(0, 0);
            super.removeMouseListener(windowManager);
            super.removeMouseMotionListener(windowManager);
        } else {
            this.isFullScreen = false;
            super.setSize(normalSize);
            super.setLocation(normalLocation);
            super.removeMouseListener(windowManager); // Ensures that the listener isnt added twice
            super.removeMouseMotionListener(windowManager); // Ensures that the listener isnt added twice
            super.addMouseListener(windowManager);
            super.addMouseMotionListener(windowManager);
        }
    }
    
    @Override
    public boolean isFullScreen() {
        return isFullScreen;
    }
    
    @Override
    public Point getLocation() {
        return isFullScreen ? normalLocation : super.getLocation();
    }
    
    @Override
    public void setLocation(Point location) {
        normalLocation = location;
        if(!isFullScreen) {
            super.setLocation(location);
        }
    }
    
    @Override
    public Dimension getSize() {
        return isFullScreen ? normalSize : super.getSize();
    }
    
    @Override
    public void setSize(Dimension size) {
        normalSize = size;
        if(!isFullScreen) {
            super.setSize(size);
        }
    }
    
    // MARK: - WindowActionsPaneDelegate

    @Override
    public void didPressCloseButton(WindowActionsPane sender) {
        unwrappedPerform(delegate, delegate -> delegate.didPressCloseWindow(this));
    }

    @Override
    public void didPressMinimizeButton(WindowActionsPane sender) {
        super.setState(JFrame.ICONIFIED);
    }

    @Override
    public void didPressStretchButton(WindowActionsPane sender) {
        setFullScreen(!isFullScreen);
    }
    
}
