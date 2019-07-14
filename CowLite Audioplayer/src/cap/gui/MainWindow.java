/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.gui.colorscheme.ColorScheme;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public class MainWindow extends JFrame implements Window {
    
    private JPanel contentPane;
    private ViewController presentedViewController;
    
    public MainWindow(ColorScheme colorScheme) {
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(1280, 720);

        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = c.BOTH;

        contentPane.add(new Menu(colorScheme), c);

        c.gridy = 2;
        c.weighty = 1;

        super.add(contentPane);
    }

    @Override
    public void presentViewController(ViewController viewController) {
        if(presentedViewController != null) {
            contentPane.remove(presentedViewController.getView());
        }
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = c.BOTH;
        
        contentPane.add(viewController.getView(), c);
        this.presentedViewController = viewController;
    }
    
}
