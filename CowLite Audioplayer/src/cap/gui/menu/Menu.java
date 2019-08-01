/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.menu;

import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Component;
import java.awt.Graphics;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.AbstractBorder;
import cap.gui.colorscheme.UILayout;

/**
 *
 * @author Wessel
 */
public class Menu extends JMenuBar {
    
    // MARK: - Associated types & constants
    
    public interface MenuDelegate {
        public void didPressSavePlaylist(Menu sender);
        public void didPressDeletePlaylist(Menu sender);
        public void didPressLayout(Menu sender);
        public void didPressHotkeys(Menu sender);
        public void didPressAbout(Menu sender);
        public void didPressFeatures(Menu sender);
    }
    
    // MARK: - Private properties
    
    private final JMenu file, settings, help;
    private WeakReference<MenuDelegate> delegate;
    
    // MARK: - Initialisers
    
    public Menu(UILayout colorScheme) {
        super.setBackground(colorScheme.frameColor());
        super.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(colorScheme.menu().menuBarBorderColor());
                g.drawLine(x, y + height - 1, x + width, y + height - 1);
            }
        });
        
        file = makeFileMenu(colorScheme);
        settings = makeSettingsMenu(colorScheme);
        help = makeHelpMenu(colorScheme);
        
        super.add(file);
        super.add(settings);
        super.add(help);
    }
    
    // MARK: - Getters & Setters
    
    public void setDelegate(MenuDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - Private functions - Menu initialisation
    
    private JMenu makeFileMenu(UILayout colorScheme) {
        JMenu menu = makeMenu("File", colorScheme);
        
        JMenuItem savePlaylist = makeMenuItem("Save playlist", colorScheme);
        savePlaylist.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressSavePlaylist(this)));
        
        JMenuItem removePlaylist = makeMenuItem("Remove playlist", colorScheme);
        removePlaylist.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressDeletePlaylist(this)));
        
        menu.add(savePlaylist);
        menu.add(removePlaylist);
        
        return menu;
    }
    
    private JMenu makeSettingsMenu(UILayout colorScheme) {
        JMenu menu = makeMenu("Settings", colorScheme);
        
        JMenuItem savePlaylist = makeMenuItem("Layout", colorScheme);
        savePlaylist.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressLayout(this)));
        
        JMenuItem removePlaylist = makeMenuItem("Hotkeys", colorScheme);
        removePlaylist.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressHotkeys(this)));
        
        menu.add(savePlaylist);
        menu.add(removePlaylist);
        
        return menu;
    }
    
    private JMenu makeHelpMenu(UILayout colorScheme) {
        JMenu menu = makeMenu("Help", colorScheme);
        
        JMenuItem savePlaylist = makeMenuItem("About", colorScheme);
        savePlaylist.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressAbout(this)));
        
        JMenuItem removePlaylist = makeMenuItem("Features", colorScheme);
        removePlaylist.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressFeatures(this)));
        
        menu.add(savePlaylist);
        menu.add(removePlaylist);
        
        return menu;
    }
    
    private JMenu makeMenu(String title, UILayout colorScheme) {
        JMenu menu = new JMenu(title);
        menu.setBorderPainted(false);
        menu.getPopupMenu().setBorder(BorderFactory.createLineBorder(colorScheme.frameColor()));
        menu.setForeground(colorScheme.defaultContentColor());
        menu.setBackground(colorScheme.frameColor());
        
        return menu;
    }
    
    private JMenuItem makeMenuItem(String title, UILayout colorScheme) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setBackground(colorScheme.menu().menuBackgroundColor());
        menuItem.setForeground(colorScheme.menu().menuTextColor());
        menuItem.setBorder(BorderFactory.createEmptyBorder());
        
        return menuItem;
    }
    
}
