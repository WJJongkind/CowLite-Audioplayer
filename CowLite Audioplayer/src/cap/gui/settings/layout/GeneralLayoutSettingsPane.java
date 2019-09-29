/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

import cap.gui.DefaultWindow;
import cap.gui.about.AboutViewController;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import cap.gui.shared.SubMenu;
import cap.gui.shared.SubMenuItem;
import cap.gui.shared.WrapLayout;
import static cap.util.QuickNDirty.preview;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public class GeneralLayoutSettingsPane extends JPanel {
    
    // MARK: - Associated types & constants
    
    public interface Delegate {
        
    }
    
    private static final class Constants {
        public static final Insets previewInsets = new Insets(0, 8, 8, 8);
        public static final Insets previewContentInsets = new Insets(2, 2, 2, 2);
        public static final int horizontalSpacingBetweenCustomizationComponents = 48;
        public static final int verticalSpacingBetweenCustomizationComponents = 16;
        public static final int minimumCustomizationComponentSize = 100;
    }
    
    // MARK: - Private properties

    private final UICustomizationComponent contentComponent;
    private final UICustomizationComponent frameComponent;
    private final JPanel previewPanel;
    private final ColorScheme previewColorScheme;
    // TODO private final SomeTextAdjustmentComponent textAdjustment;
    
    // MARK: - Initialisers
    
    public GeneralLayoutSettingsPane(ColorScheme colorScheme, ColorScheme previewColorScheme) {
        this.previewColorScheme = previewColorScheme;
        
        contentComponent = new UICustomizationComponent(colorScheme, "Primary content / text color: ", colorScheme.general().getContentColor());
        contentComponent.setDelegate(e -> showColorPickerForContentColor());
        frameComponent = new UICustomizationComponent(colorScheme, "Window background color: ", colorScheme.general().getFrameColor());
        frameComponent.setDelegate(e -> showColorPickerForFrameColor());
        previewPanel = makePreviewPanel(colorScheme, previewColorScheme);
        
        super.setBackground(colorScheme.general().getFrameColor());
        
        layoutComponents(colorScheme);
    }
    
    // MARK: - Private methods
    
    private void layoutComponents(ColorScheme colorScheme) {
        super.setLayout(new GridBagLayout());
        
        layoutCustomizationComponents(colorScheme);
        layoutPreviewPanel();
    }
    
    private void layoutCustomizationComponents(ColorScheme colorScheme) {
        contentComponent.setMinimumSize(new Dimension(Constants.minimumCustomizationComponentSize, colorScheme.font().m().getSize()));
        frameComponent.setMinimumSize(new Dimension(Constants.minimumCustomizationComponentSize, colorScheme.font().m().getSize()));
        
        JPanel container = new JPanel();
        container.setBackground(new Color(0, 0, 0, 0));
        
        container.setLayout(new WrapLayout(FlowLayout.CENTER, Constants.horizontalSpacingBetweenCustomizationComponents, Constants.verticalSpacingBetweenCustomizationComponents));
        container.add(contentComponent);
        container.add(frameComponent);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = c.HORIZONTAL;
        
        super.add(container, c);
    }
    
    private void layoutPreviewPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = c.BOTH;
        c.insets = Constants.previewInsets;
        
        super.add(previewPanel, c);
    }
    
    private JPanel makePreviewPanel(ColorScheme colorScheme, ColorScheme previewColorScheme) {
        // Main screen content
        DefaultWindow previewWindow = new DefaultWindow(previewColorScheme);
        previewWindow.presentViewController(new AboutViewController(previewColorScheme));
        
        // Dummy menu
        SubMenu subMenu = new SubMenu("File", colorScheme);
        
        SubMenuItem savePlaylist = new SubMenuItem("Save playlist", colorScheme, e -> {});
        SubMenuItem removePlaylist = new SubMenuItem("Remove playlist", colorScheme, e -> {});
        
        subMenu.add(savePlaylist);
        subMenu.add(removePlaylist);
        
        previewWindow.setSubMenus(subMenu);
        
        // Make a JPanel with a nice border around it to embed the preview
        JPanel previewPanel = new JPanel();
        previewPanel.setBackground(colorScheme.general().getContentColor());
        previewPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = Constants.previewContentInsets;
        previewPanel.add(previewWindow.getContentPane(), c);
        
        return previewPanel;
    }
    
    private void showColorPickerForContentColor() {
        // TODO show window with SexyColorPickerViewController. This should be a shared instance between the view tabs. Hide it (if it's already presented), set location to that of the pressed button and set delegate
    }
    
    private void showColorPickerForFrameColor() {
        
    }
    
}
