/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import cap.gui.shared.Button;
import cap.gui.shared.SexyColorPicker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/**
 *
 * @author Wessel
 */
abstract class LayoutSettingsPane extends JPanel {
    
    // MARK: - Associated types & constants
    
    private static final class Layout {
        public static final int edgeInset = 8;
        public static final int marginBetweenButtons = 24;
    }
    
    // MARK: - Private properties
    
    private final PopupFactory popupFactory;
    private final SexyColorPicker colorPicker;
    private final Button confirmButton, cancelButton;
    private final JPanel popupPanel;
    private Popup shownPopup;
    
    // MARK: - Initialisers
    
    protected LayoutSettingsPane(ColorScheme colorScheme) {
        popupFactory = new PopupFactory();
        colorPicker = new SexyColorPicker();
        confirmButton = new Button("Confirm", colorScheme.defaultButtonColorScheme());
        cancelButton = new Button("Cancel", colorScheme.defaultButtonColorScheme());
        cancelButton.addActionListener(e -> {
            if(shownPopup != null) {
                shownPopup.hide();
                shownPopup = null;
            }
        });
        popupPanel = new JPanel();
        popupPanel.setBackground(colorScheme.frameColor());
        
        layoutPopup();
    }
    
    // MARK: - Protected methods
    
    protected final void showPopup(JComponent component, ActionListener onConfirmPressed) {
        confirmButton.removeActionListeners();
        confirmButton.addActionListener(onConfirmPressed);
        
        if(shownPopup != null) {
            shownPopup.hide();
                shownPopup = null;
        }
        
        shownPopup = popupFactory.getPopup(component, popupPanel, 0, 0);
        shownPopup.show();
    } 
    
    // MARK: - Private methods
    
    private void layoutPopup() {
        popupPanel.setLayout(new GridBagLayout());
        popupPanel.setPreferredSize(new Dimension(500, 500));
        
        layoutColorPicker();
        layoutConfirmButton();
        layoutCancelButton();
    }
    
    private void layoutColorPicker() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = c.BOTH;
        c.insets = new Insets(Layout.edgeInset, Layout.edgeInset, Layout.edgeInset, Layout.edgeInset);
        
        popupPanel.add(colorPicker, c);
    }
    
    private void layoutConfirmButton() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = c.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, Layout.marginBetweenButtons, Layout.edgeInset, Layout.edgeInset);
        
        popupPanel.add(confirmButton, c);
    }
    
    private void layoutCancelButton() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = c.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, Layout.edgeInset, Layout.edgeInset, 0);
        
        popupPanel.add(cancelButton, c);
    }
    
    
    
    
    
    
    
    
    
    
    
    public static void main(String[] args) throws IOException {
        LayoutSettingsPane pane = new LayoutSettingsPane(new DarkMode()) {
        };
        
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        frame.setVisible(true);
        
        JPanel panel = new JPanel();
        panel.setSize(1000, 1000);
        frame.getContentPane().add(panel);
        panel.setBackground(Color.cyan);
        
        pane.showPopup(panel, e -> System.out.println("OHi there"));
        
    }
    
}
