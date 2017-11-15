package cap.gui.settings;

import cap.control.GlobalKeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * (c) Copyright
 * This class listens to what the hotkeys are that the user is setting. 
 */
public class SettingsKeyListener implements KeyListener
{

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
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.playfield)
            SettingsMenu.playfield.setText(GlobalKeyListener.lastpressed);
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.pausefield)
            SettingsMenu.pausefield.setText(GlobalKeyListener.lastpressed);
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.stopfield)
            SettingsMenu.stopfield.setText(GlobalKeyListener.lastpressed);
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.volumeupfield)
            SettingsMenu.volumeupfield.setText(GlobalKeyListener.lastpressed);
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.volumedownfield)
            SettingsMenu.volumedownfield.setText(GlobalKeyListener.lastpressed);
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.previousfield)
            SettingsMenu.previousfield.setText(GlobalKeyListener.lastpressed);
        if(SettingsMenu.frame.getFocusOwner() == SettingsMenu.nextfield)
            SettingsMenu.nextfield.setText(GlobalKeyListener.lastpressed);
    }
    
}
