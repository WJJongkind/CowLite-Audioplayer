/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.core.CowLiteAudioPlayer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Wessel
 */
public class OverlaySettings extends JFrame implements ActionListener
{
    private JButton okay;
    private JCheckBox defaultEnabled;
    private JLabel size, setDefault;
    private JComboBox selectSize;
    private boolean isDefault;
    
    public OverlaySettings()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        
        String[] theSizes = {"small", "medium", "large"};
        selectSize = new JComboBox(theSizes);
        okay = new JButton("Save");
        defaultEnabled = new JCheckBox();
        defaultEnabled.addActionListener(this);
        size = new JLabel("Overlay size:");
        setDefault = new JLabel("Overlay enabled by default:");
        
        okay.addActionListener(this);
        
        setSettings();
        
        makeGui();
        
    }
    
    private void setSettings()
    {
        try{
            BufferedReader red = new BufferedReader(new FileReader(CowLiteAudioPlayer.docPath + "\\CowLite Audio Player\\resources\\launchersettings\\overlay.txt"));
            selectSize.setSelectedItem(red.readLine());
            
            isDefault = Boolean.parseBoolean(red.readLine());
            defaultEnabled.setSelected(isDefault);
            red.close();
        }catch(Exception e){}
    }
    
    private void makeGui()
    {
        GridBagConstraints c = new  GridBagConstraints();
        c.fill = c.BOTH;
        c.gridy = 1;
        c.gridx = 1;
        
        add(setDefault, c);
        
        c.gridx = 2;
        
        add(defaultEnabled, c);
        
        c.gridy = 2;
        
        add(selectSize, c);
        
        c.gridx = 1;
        
        add(size, c);
        
        c.gridwidth = 2;
        c.gridy = 3;
        c.fill = c.NONE;
        
        add(okay, c);
        
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == okay)
        {
            CowLiteAudioPlayer.gui.setOverlaySettings((String)selectSize.getSelectedItem());
            CowLiteAudioPlayer.gui.toggleOverlay(isDefault);
            try{
                PrintStream out = new PrintStream(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\overlay.txt");
                out.println((String)selectSize.getSelectedItem());
                out.println(isDefault);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }catch(Exception f){}
        }
        
        if(e.getSource() == defaultEnabled)
            isDefault = !isDefault;
    }
}
