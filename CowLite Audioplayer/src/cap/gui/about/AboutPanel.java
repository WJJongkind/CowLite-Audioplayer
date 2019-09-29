/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.about;

import cap.gui.shared.SexyScrollPane;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
class AboutPanel extends SexyScrollPane {
    
    public AboutPanel(ColorScheme layout) {
        super(layout.general().getContentColor());
        
        JTextArea textArea = new JTextArea();
        textArea.setBackground(layout.general().getFrameColor());
        textArea.setForeground(layout.general().getContentColor());
        textArea.setEditable(false);
        textArea.setBorder(null);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(layout.font().bold().l().get());
        
        textArea.setText("To be written.");
        
        super.setViewport(super.createViewport());
        super.getViewport().add(textArea);
        super.getViewport().setBackground(layout.general().getContentColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder());
        super.setPreferredSize(super.getMinimumSize());
    }
    
}
