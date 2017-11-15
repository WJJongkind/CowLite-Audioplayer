package cap.gui.settings;


import cap.gui.settings.SettingsMenu;
import cap.control.GlobalKeyListener;
import cap.core.CowLiteAudioPlayer;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * (c) Copyright
 * This class saves or discards the selected hotkey settings.
 */
public class SettingsListener implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == SettingsMenu.save)
        {
            ArrayList<String> list = new ArrayList<>();
            list.add(SettingsMenu.playfield.getText());
            list.add(SettingsMenu.pausefield.getText());
            list.add(SettingsMenu.stopfield.getText());
            list.add(SettingsMenu.volumeupfield.getText());
            list.add(SettingsMenu.volumedownfield.getText());
            list.add(SettingsMenu.nextfield.getText());
            list.add(SettingsMenu.previousfield.getText());
            
            try
            {
                PrintStream out1 = System.out;
                PrintStream out = new PrintStream(new FileOutputStream(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\controls.txt")));
                System.setOut(out);
                System.out.println("settings");
                for(int i = 0; i < list.size(); i++)
                {
                    System.out.println(list.get(i));
                }
                
                GlobalKeyListener.play = list.get(0);
                GlobalKeyListener.pause = list.get(1);
                GlobalKeyListener.stop = list.get(2);
                GlobalKeyListener.volumeup = list.get(3);
                GlobalKeyListener.volumedown = list.get(4);
                GlobalKeyListener.next = list.get(5);
                GlobalKeyListener.previous = list.get(6);
            }catch(Exception f){}
            SettingsMenu.frame.dispatchEvent(new WindowEvent(SettingsMenu.frame, WindowEvent.WINDOW_CLOSING));
            list = null;
        }
        
        if(e.getSource() == SettingsMenu.discard)
        {
            SettingsMenu.frame.dispatchEvent(new WindowEvent(SettingsMenu.frame, WindowEvent.WINDOW_CLOSING));
        }
    }
    
}
