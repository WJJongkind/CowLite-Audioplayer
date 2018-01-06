/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public abstract class DefaultPanel extends JPanel
{
    protected DefaultWindow parent;
    
    public void exit()
    {
        parent.exit();
    }
    
    public void setParent(DefaultWindow parent)
    {
        System.out.println(parent);
        this.parent = parent;
    }
}
