package cap.gui;

import cap.core.CoreTime;
import cap.core.CowLiteAudioPlayer;
import cap.gui.overlay.OverlaySettings;
import cap.gui.settings.GraphicalSettingsMenu;
import cap.gui.settings.SettingsMenu;
import cap.util.InterfaceIO;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * (c) Copyright
 * This class listens for actions happening on the JMenu of the interface.
 */
public class MenuListener implements ActionListener
{
    GraphicalSettingsMenu menu;

    /**
     * If a menuitem has been clicked, this method gets called
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == GraphicalInterface.saveList) InterfaceIO.saveFile();
        if(e.getSource() == GraphicalInterface.removeList) InterfaceIO.removeFile();
        if(e.getSource() == GraphicalInterface.about)
        {
            try
            {
                Desktop.getDesktop().edit(new File(CowLiteAudioPlayer.docPath + "/CowLite Audio Player/resources/infofiles/about.txt"));
            }catch (IOException f){System.out.println(f + "JMenuListenerActionPerformed");}
        }
        if(e.getSource() == GraphicalInterface.help)
        {
            try
            {
                Desktop.getDesktop().edit(new File(CowLiteAudioPlayer.docPath + "/CowLite Audio Player/resources/infofiles/controls.txt"));
            }catch (IOException f){System.out.println(f + "JMenuListenerActionPerformed2");}
        }
        if(e.getSource() == GraphicalInterface.setHotkeys)
        {
            SettingsMenu menu = new SettingsMenu();
        }
        if(e.getSource() == GraphicalInterface.setGraphics)
        {
            menu = new GraphicalSettingsMenu();
            menu = null;
        }
        
        if(e.getSource() == GraphicalInterface.setOverlay)
        {
            new OverlaySettings();
        }
        
        if(e.getSource() == GraphicalInterface.importYoutube)
        {
            String result = JOptionPane.showInputDialog("Enter the ID of the YouTube playlist, please.");
            String name = JOptionPane.showInputDialog("Enter the name that you want to assign to the playlist, please.");
            GraphicalInterface.uptodate2 = false;
            CoreTime.update = true;
            InterfaceIO.saveYouTube(name, result);
        }
    }
}
