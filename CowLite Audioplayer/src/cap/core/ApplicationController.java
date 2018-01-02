/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.core.audio.AudioController;
import cap.core.audio.AudioPlayer;
import cap.gui.GUIHandler;

/**
 *
 * @author Wessel
 */
public class ApplicationController 
{
    private GUIHandler gui;
    private AudioController audio;
    
    public void nextSongEvent()
    {
        audio.getPlayer().changeSong(1);
    }
    
    public void previousSongEvent()
    {
        audio.getPlayer().changeSong(-1);
    }
    
    public void pauseEvent()
    {
        System.out.println("pauseevent");
        AudioPlayer player = audio.getPlayer();
        if(player.isPaused())
        {
            System.out.println("unpausing");
            player.setPaused(false);
            gui.getGui().setPauseButton();
        }
        else
        {
            System.out.println("pausing");
            player.setPaused(true);
            gui.getGui().setPlayButton();
        }
    }
    
    public void playEvent()
    {
        System.out.println("playevent");
        AudioPlayer player = audio.getPlayer();
        
        
        if(player != null && player.isPaused())
        {
            System.out.println("infirst");
            player.setPaused(false);
            gui.getGui().setPauseButton();
        }
        else if(player != null && player.getIndex() > -1)
        {
            System.out.println("insecond");
            player.stop();
            player.play();
            gui.getGui().setPauseButton();
        }
        else if(player != null)
        {
            System.out.println("selecting...");
            player.selectSong(0);
            System.out.println("playing...");
        }
        else
        {
            System.out.println("Random list time");
            audio.loadRandomList();
            gui.getGui().setPauseButton();
        }
    }
    
    public void stopEvent()
    {
        audio.getPlayer().stop();
        gui.getGui().setPlayButton();
    }
    
    public void changeVolumeEvent(int newVolume)
    {
        audio.getPlayer().changeVolume(newVolume);
        
        //TODO change volume of slider
    }
    
    public void toggleOverlay()
    {
        gui.getGui().toggleOverlay();
    }
    
    public void repositionOverlay(int x, int y)
    {
        gui.getGui().repositionOverlay(x, y);
    }
    
    public void setGUI(GUIHandler gui)
    {
        this.gui = gui;
    }
    
    public void setAudioController(AudioController audio)
    {
        this.audio = audio;
    }
}
