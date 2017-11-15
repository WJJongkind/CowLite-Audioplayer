/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Wessel
 */
public class TranslucentFrame extends JFrame
{
    private InfoComponent info;
    public TranslucentFrame() {
        super("TranslucentWindow");
        setLayout(new GridBagLayout());
        
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add a sample button.
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.fill = c.BOTH;
        c.gridy = 1;
        c.anchor = GridBagConstraints.NORTH;
        info = new InfoComponent();
        info.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        OverlayListener listener = new OverlayListener(info);
        info.addMouseListener(listener);
        info.addMouseMotionListener(listener);
        
        add(info, c);
    }

    public InfoComponent getInfoComponent()
    {
        return info;
    }
}
