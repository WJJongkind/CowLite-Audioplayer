package cap.gui.settings;

import cap.control.GlobalKeyListener;
import java.awt.*;
import javax.swing.*;

/**
 * (c) Copyright
 * shows a menu presenting all the selected hotkeys
 */
public class SettingsMenu
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
    
    /**
     * creates a frame with all the selected hotkeys
     */
    public SettingsMenu()
    {   
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
        playfield = new JTextField(GlobalKeyListener.play);
        pausefield = new JTextField(GlobalKeyListener.pause);
        stopfield = new JTextField(GlobalKeyListener.stop);
        previousfield = new JTextField(GlobalKeyListener.previous);
        nextfield = new JTextField(GlobalKeyListener.next);
        volumeupfield = new JTextField(GlobalKeyListener.volumeup);
        volumedownfield = new JTextField(GlobalKeyListener.volumedown);
        
        SettingsKeyListener listener = new SettingsKeyListener();
        playfield.addKeyListener(listener);
        pausefield.addKeyListener(listener);
        stopfield.addKeyListener(listener);
        volumeupfield.addKeyListener(listener);
        volumedownfield.addKeyListener(listener);
        nextfield.addKeyListener(listener);
        previousfield.addKeyListener(listener);
        
        playlabel = new JLabel("Play Hotkey: ");
        pauselabel = new JLabel("Pause Hotkey: ");
        stoplabel = new JLabel("Stop Hotkey: ");
        previouslabel = new JLabel("Previous Hotkey: ");
        nextlabel = new JLabel("Next Hotkey: ");
        volumeuplabel = new JLabel("Volume Up Hotkey: ");
        volumedownlabel = new JLabel("Volume Down Hotkey: ");
        
        save = new JButton("save");
        discard = new JButton("discard");
        SettingsListener actions = new SettingsListener();
        save.addActionListener(actions);
        discard.addActionListener(actions);
        
        
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
}
