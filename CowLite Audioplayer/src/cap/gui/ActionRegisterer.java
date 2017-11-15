package cap.gui;

import cap.core.CoreTime;
import cap.core.CowLiteAudioPlayer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import static cap.core.CowLiteAudioPlayer.player;
import cap.util.InterfaceIO;

/**
 * (c) Copyright
 * This class handles all the button-events of GraphicalInterface (the gui)
 */
public class ActionRegisterer implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == GraphicalInterface.playButton)
        {
                if(player.isPlaying())
                {
                    if(player.isPaused())
                        player.setPaused(false);
                    else
                        player.setPaused(true);
                }
                else
                    player.play();
        }
            
        if(e.getSource() == GraphicalInterface.stopButton)
            CowLiteAudioPlayer.player.stop();
        
        if(e.getSource() == GraphicalInterface.nextButton)
        {
            player.changeSong(1);
            player.stop();
            player.play();
            GraphicalInterface.songlist.setSelectedIndex(player.getIndex());
        }
        
        if(e.getSource() == GraphicalInterface.prevButton)
        {
            player.changeSong(-1);
            player.stop();
            player.play();
            GraphicalInterface.songlist.setSelectedIndex(player.getIndex());
        }
        
        if(e.getSource() == GraphicalInterface.clearButton)
        {
            player.stop();
            player.clearList();
            GraphicalInterface.savedListText.clearSelection();
            InterfaceIO.setPlayButton();
            GraphicalInterface.uptodate = false;
            CoreTime.update = true;
        }
        
        if(e.getSource() == GraphicalInterface.shuffleButton)
        {
            InterfaceIO.setAlphabetic();
            player.stop();
            player.shuffle();
            player.play();
            
            if(player.getShuffled()) 
                InterfaceIO.setShufflePressed();
            else
                InterfaceIO.setShuffle();
        }
        
        if(e.getSource() == GraphicalInterface.exitButton)
            GUIHandler.frame.dispatchEvent(new WindowEvent(GUIHandler.frame, WindowEvent.WINDOW_CLOSING));
        
        if(e.getSource() == GraphicalInterface.maximizeButton)
        {
            if(!GraphicalInterface.maximized)
            {
                GraphicalInterface.oldDimension = new Dimension(GUIHandler.frame.getWidth(), GUIHandler.frame.getHeight());
                GraphicalInterface.oldPoint = new Point(GUIHandler.frame.getLocation());
                GUIHandler.frame.setExtendedState(GUIHandler.frame.MAXIMIZED_BOTH); 
                GraphicalInterface.maximized = true;
            }
            else
            {
                GraphicalInterface.maximized = false;
                GUIHandler.frame.setSize(GraphicalInterface.oldDimension);
                GUIHandler.frame.setLocation(GraphicalInterface.oldPoint);
            }
        }
        
        if(e.getSource() == GraphicalInterface.alphabeticButton)
        {
            InterfaceIO.setShuffle();
            
            player.stop();
            player.alphabetical();
            player.play();
            
            if(player.getAlphabetical())
                InterfaceIO.setAlphabeticPressed();
            else
                InterfaceIO.setAlphabetic();
        }
    }
}
