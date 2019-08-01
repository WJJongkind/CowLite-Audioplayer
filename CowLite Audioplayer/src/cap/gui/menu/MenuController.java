/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.menu;

import cap.core.services.PlaylistService;
import cap.gui.ViewController;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class MenuController implements ViewController, Menu.MenuDelegate {
    
    // MARK: - Associated types
    
    public interface MenuControllerDelegate {
        public void didPressShowAbout(MenuController menuController);
        public void didPressHotkeys(MenuController menuController);
        public void didPressLayout(MenuController menuController);
        public void didPressFeatures(MenuController menuController);
    }

    // MARK: - Private properties
    
    private final Menu menu;
    private final MenuContextInterface menuContext;
    private final PlaylistService playlistService;
    private WeakReference<MenuControllerDelegate> delegate;
    
    // MARK: - Initialisers

    public MenuController(ColorScheme colorScheme, MenuContextInterface menuContext, PlaylistService playlistService) {
        this.playlistService = playlistService;
        this.menuContext = menuContext;
        menu = new Menu(colorScheme);
        menu.setDelegate(this);
    }

    // MARK: - ViewController
    
    @Override
    public Menu getView() {
        return menu;
    }
    
    // MARK: - Public functions
    
    public void setDelegate(MenuControllerDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    // MARK: - MenuDelegate
    
    @Override
    public void didPressSavePlaylist(Menu sender) {
        JFileChooser chooser = null;
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            chooser = new JFileChooser();
            UIManager.setLookAndFeel(previousLF);
        } catch (Exception e) {
            chooser = new JFileChooser();
        }
        if(chooser.showSaveDialog(menu) == JFileChooser.APPROVE_OPTION) {
            try {
                menuContext.getCurrentPlaylist().setName(chooser.getSelectedFile().getName());
                playlistService.savePlayList(menuContext.getCurrentPlaylist(), chooser.getSelectedFile());
            } catch (FileNotFoundException ex) {
                // TODO show error message
            }
        }
    }

    @Override
    public void didPressDeletePlaylist(Menu sender) {
        try {
            menuContext.getPlaylistStore().removePlaylist(menuContext.getCurrentPlaylist());
        } catch (IOException ex) {
            // TODO show some usefull user feedback
            ex.printStackTrace();
        }
    }

    @Override
    public void didPressLayout(Menu sender) {
        unwrappedPerform(delegate, delegate -> delegate.didPressLayout(this));
    }

    @Override
    public void didPressHotkeys(Menu sender) {
        unwrappedPerform(delegate, delegate -> delegate.didPressHotkeys(this));
    }

    @Override
    public void didPressAbout(Menu sender) {
        unwrappedPerform(delegate, delegate -> delegate.didPressShowAbout(this));
    }

    @Override
    public void didPressFeatures(Menu sender) {
        unwrappedPerform(delegate, delegate -> delegate.didPressFeatures(this));
    }

}
