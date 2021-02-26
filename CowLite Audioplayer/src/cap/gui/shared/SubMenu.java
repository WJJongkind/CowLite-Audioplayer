/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.ColorScheme;
import javax.swing.BorderFactory;
import javax.swing.JMenu;

/**
 *
 * @author Wessel
 */
public class SubMenu extends JMenu {

    public SubMenu(String title, ColorScheme colorScheme) {
        super(title);

        super.setBorderPainted(false);
        super.getPopupMenu().setBorder(BorderFactory.createLineBorder(colorScheme.general().getFrameColor()));
        super.setForeground(colorScheme.general().getContentColor());
        super.setBackground(colorScheme.general().getFrameColor());
    }

}
