package cap.core.audio;

import cap.core.CoreTime;
import cap.core.CowLiteAudioPlayer;
import cap.gui.GraphicalInterface;
import cap.util.InterfaceIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * (c) Copyright
 * This class looks for if songs should be removed from a playlist or if
 * playlists should be removed.
 */
public class ControlListener implements KeyListener
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
        if(KeyEvent.VK_DELETE == e.getKeyCode() && GraphicalInterface.songlist.getSelectedIndex() > -1)
        {
            CowLiteAudioPlayer.player.removeSong(GraphicalInterface.songlist.getSelectedIndex());
            GraphicalInterface.uptodate = false;
            CoreTime.update = true;
        }
        if(KeyEvent.VK_DELETE == e.getKeyCode() && GraphicalInterface.savedListText.getSelectedIndex() > -1)
        {
            InterfaceIO.removeFile();
        }
    }
    
}
