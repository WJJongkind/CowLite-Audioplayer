/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import cap.gui.colorscheme.UILayout;

/**
 *
 * @author Wessel
 */
public class PlaylistsSplitPane extends JSplitPane {
    
    public PlaylistsSplitPane(UILayout colorScheme, int orientation, Component first, Component second) {
        super(orientation, first, second);
        
        super.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void setBorder(Border b){}
                };
            }
        });
        
        super.setBorder(null);
        super.setBackground(colorScheme.frameColor());
    }
    
}
