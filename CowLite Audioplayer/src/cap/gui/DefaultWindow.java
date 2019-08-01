/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.core.services.PlaylistService;
import cap.gui.WindowActionsPane.WindowActionsPaneDelegate;
import cap.gui.menu.MenuController;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;
import cap.gui.menu.MenuContextInterface;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class DefaultWindow extends JFrame implements Window, WindowActionsPaneDelegate {
    
    // MARK: - Constants and associated types
    
    private static final Dimension minimumSize = new Dimension(400, 200);
    private static final WindowManager windowManager = new WindowManager();
    
    // MARK: - Private properties
    
    private final MenuController menuController; // TODO inject me
    private boolean maximizedState = false; // TODO inject & store me
    private Dimension normalSize = new Dimension(1280, 720); // TODO inject & store me
    private Point normalLocation = new Point(200, 200);
    private WeakReference<WindowDelegate> delegate;
    
    // MARK: - UI elements
    
    private JPanel contentPane;
    private WindowActionsPane windowActionsPane;
    private ViewController presentedViewController;
    
    // MARK: - Initialisers
    
    public DefaultWindow(ColorScheme colorScheme, MenuContextInterface menuContext) {
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(normalSize);
        super.setBackground(colorScheme.frameColor());
        super.getContentPane().setBackground(colorScheme.frameColor());
        super.setUndecorated(true);
        super.addMouseListener(windowManager);
        super.addMouseMotionListener(windowManager);
        super.setMinimumSize(minimumSize);
        
        menuController = new MenuController(colorScheme, menuContext, new PlaylistService(menuContext.getPlaylistStore())); // TODO make a setMenu option... Perhaps change the entire way menus are handled.
        windowActionsPane = new WindowActionsPane(colorScheme);
        windowActionsPane.setDelegate(this);
        
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBackground(colorScheme.frameColor());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = c.BOTH;

        contentPane.add(menuController.getView(), c);
        
        c.gridx = 2;
        c.weightx = 0;
        
        contentPane.add(windowActionsPane, c);

        super.add(contentPane);
    }
    
    // MARK: - Window

    @Override
    public void presentViewController(ViewController viewController) {
        if(presentedViewController != null) {
            contentPane.remove(presentedViewController.getView());
        }
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = c.BOTH;
        
        contentPane.add(viewController.getView(), c);
        this.presentedViewController = viewController;
        
        super.revalidate();
        super.repaint();
    }

    @Override
    public void setDelegate(WindowDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    @Override
    public void setMenu(JComponent menu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        if(maximizedState) {
            maximizedState = false;
            super.setSize(normalSize);
            super.setLocation(normalLocation);
            super.addMouseListener(windowManager); // TODO disable if is fullscreen
            super.addMouseMotionListener(windowManager);
        } else {
            normalSize = getSize();
            normalLocation = getLocation();
            maximizedState = true;
            super.setSize(getToolkit().getScreenSize());
            super.setLocation(0, 0);
            super.removeMouseListener(windowManager);
            super.removeMouseMotionListener(windowManager);
        }
    }
    
}
