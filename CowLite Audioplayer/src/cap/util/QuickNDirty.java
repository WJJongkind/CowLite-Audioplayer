/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

/**
 * NOTE, this code is not ment for usage in production code
 * Just to quickly instantiate commonly used objects on the fly when testing a class by
 * using a temporary public static void main method.
 * @author Wessel
 */
public class QuickNDirty {
    
   @Deprecated
   public static JFrame makeFrame() {
       JFrame frame = new JFrame();
       frame.setSize(400, 300);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       return frame;
   }
   
   @Deprecated
   public static void preview(Container container) {
       JFrame frame = makeFrame();
       frame.getContentPane().setLayout(new GridBagLayout());
       
       GridBagConstraints c = new GridBagConstraints();
       c.gridx = 1;
       c.gridy = 1;
       c.weightx = 1;
       c.weighty = 1;
       c.gridwidth = 1;
       c.gridheight = 1;
       c.fill = c.BOTH;
       
       frame.add(container, c);
       frame.setVisible(true);
   }
}
