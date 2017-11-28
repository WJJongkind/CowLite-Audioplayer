package cap.gui.settings;

import cap.control.GlobalKeyListener;
import cap.core.PropertiesManager;
import cap.util.IO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;
import javax.swing.*;

/**
 * (c) Copyright
 * shows a menu presenting all the selected hotkeys
 */
public class SettingsMenu implements KeyListener, ActionListener
{
    public static JFrame frame;
    public static JTextField playfield;
    public static JTextField pausefield;
    public static JTextField stopfield;
    public static JTextField previousfield;
    public static JTextField nextfield;
    public static JTextField volumeupfield;
    public static JTextField volumedownfield;
    public static JLabel playlabel;
    public static JLabel pauselabel;
    public static JLabel stoplabel;
    public static JLabel previouslabel;
    public static JLabel nextlabel;
    public static JLabel volumeuplabel;
    public static JLabel volumedownlabel;
    public static JButton save;
    public static JButton discard;
    //private GlobalKeyListener keyl = new GlobalKeyListener();
    
    private final Map<String, String> hotkeys;
    private final PropertiesManager properties;
            
    /**
     * creates a frame with all the selected hotkeys
     */
    public SettingsMenu(PropertiesManager properties)
    {   
        this.hotkeys = properties.getControlProperties();
        this.properties = properties;
        
        frame = new  JFrame("Settings");
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GlobalKeyListener.settingsOn = true;
                    frame.addWindowListener(new java.awt.event.WindowAdapter()
            {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent)
            {
                GlobalKeyListener.settingsOn = false;
                frame.setVisible(false);
                frame.dispose();
            }
        });  
        
        //Initialise objects
        playfield = new JTextField(hotkeys.get("play"));
        pausefield = new JTextField(hotkeys.get("pause"));
        stopfield = new JTextField(hotkeys.get("stop"));
        previousfield = new JTextField(hotkeys.get("previous"));
        nextfield = new JTextField(hotkeys.get("next"));
        volumeupfield = new JTextField(hotkeys.get("volumeUp"));
        volumedownfield = new JTextField(hotkeys.get("volumeDown"));
        
        playfield.addKeyListener(this);
        pausefield.addKeyListener(this);
        stopfield.addKeyListener(this);
        volumeupfield.addKeyListener(this);
        volumedownfield.addKeyListener(this);
        nextfield.addKeyListener(this);
        previousfield.addKeyListener(this);
        
        playlabel = new JLabel("Play Hotkey: ");
        pauselabel = new JLabel("Pause Hotkey: ");
        stoplabel = new JLabel("Stop Hotkey: ");
        previouslabel = new JLabel("Previous Hotkey: ");
        nextlabel = new JLabel("Next Hotkey: ");
        volumeuplabel = new JLabel("Volume Up Hotkey: ");
        volumedownlabel = new JLabel("Volume Down Hotkey: ");
        
        save = new JButton("save");
        discard = new JButton("discard");
        save.addActionListener(this);
        discard.addActionListener(this);
        
        
        GridBagConstraints c = new GridBagConstraints();
        frame.setLayout(new GridBagLayout());
        Container controller = frame.getContentPane();
        
        c = insertComponent(c, c.HORIZONTAL, 1, 1, 0, 0, 1, 1);
        controller.add(playlabel, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 2, c.weightx, c.weighty, 1, 1);
        controller.add(pauselabel, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 3, c.weightx, c.weighty, 1, 1);
        controller.add(stoplabel, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 4, c.weightx, c.weighty, 1, 1);
        controller.add(nextlabel, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 5, c.weightx, c.weighty, 1, 1);
        controller.add(previouslabel, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 6, c.weightx, c.weighty, 1, 1);
        controller.add(volumeuplabel, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 7, c.weightx, c.weighty, 1, 1);
        controller.add(volumedownlabel, c);
        
        c = insertComponent(c, c.HORIZONTAL, 2, 1, 0, 0, 1, 1);
        controller.add(playfield, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 2, c.weightx, c.weighty, 1, 1);
        controller.add(pausefield, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 3, c.weightx, c.weighty, 1, 1);
        controller.add(stopfield, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 4, c.weightx, c.weighty, 1, 1);
        controller.add(nextfield, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 5, c.weightx, c.weighty, 1, 1);
        controller.add(previousfield, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 6, c.weightx, c.weighty, 1, 1);
        controller.add(volumeupfield, c);
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 7, c.weightx, c.weighty, 1, 1);
        controller.add(volumedownfield, c);
        
        c = insertComponent(c, c.HORIZONTAL, c.gridx, 8, c.weightx, c.weighty, 1, 1);
        controller.add(discard, c);
        c = insertComponent(c, c.HORIZONTAL, 1, 8, c.weightx, c.weighty, 1, 1);
        controller.add(save, c);
        
        frame.setVisible(true);
    }
    
    private GridBagConstraints insertComponent(GridBagConstraints c, int fill, int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight)
    {
       c.fill = fill;
       c.gridx = gridx;
       c.gridy = gridy;
       c.weightx = weightx;
       c.weighty = weighty;
       c.gridwidth = gridwidth;
       c.gridheight = gridheight;
       return c;
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(frame.getFocusOwner() == SettingsMenu.playfield)
            playfield.setText(GlobalKeyListener.lastPressed);
        if(frame.getFocusOwner() == SettingsMenu.pausefield)
            pausefield.setText(GlobalKeyListener.lastPressed);
        if(frame.getFocusOwner() == SettingsMenu.stopfield)
            stopfield.setText(GlobalKeyListener.lastPressed);
        if(frame.getFocusOwner() == SettingsMenu.volumeupfield)
            volumeupfield.setText(GlobalKeyListener.lastPressed);
        if(frame.getFocusOwner() == SettingsMenu.volumedownfield)
            volumedownfield.setText(GlobalKeyListener.lastPressed);
        if(frame.getFocusOwner() == SettingsMenu.previousfield)
            previousfield.setText(GlobalKeyListener.lastPressed);
        if(frame.getFocusOwner() == SettingsMenu.nextfield)
            nextfield.setText(GlobalKeyListener.lastPressed);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == SettingsMenu.save)
        {
            hotkeys.replace("play", playfield.getText());
            hotkeys.replace("pause", pausefield.getText());
            hotkeys.replace("stop", stopfield.getText());
            hotkeys.replace("volumeUp", volumeupfield.getText());
            hotkeys.replace("volumeDown", volumedownfield.getText());
            hotkeys.replace("next", nextfield.getText());
            hotkeys.replace("previous", previousfield.getText());
            
            properties.storeProperties(hotkeys);
            SettingsMenu.frame.dispatchEvent(new WindowEvent(SettingsMenu.frame, WindowEvent.WINDOW_CLOSING));
        }
        
        if(e.getSource() == SettingsMenu.discard)
        {
            SettingsMenu.frame.dispatchEvent(new WindowEvent(SettingsMenu.frame, WindowEvent.WINDOW_CLOSING));
        }
    }
    
}
