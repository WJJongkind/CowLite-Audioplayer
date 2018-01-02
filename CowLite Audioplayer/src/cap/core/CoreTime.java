package cap.core;

import cap.core.audio.AudioController;
import cap.core.audio.AudioPlayer;
import cap.core.audio.FileAudioPlayer;
import cap.core.audio.MetaData;
import cap.gui.GUIHandler;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * (c) Copyright
 * This class makes sure the GUI gets updated and that song changes happen.
 * @author Wessel Jongkind
 */
public class CoreTime implements ActionListener
{ 
    private final AudioController AUDIO;
    private final GUIHandler GUI;
    private int switchedIndex = -1;
    private List<?> playlist;
    
    public CoreTime(GUIHandler handler, AudioController controller)
    {
        AUDIO = controller;
        GUI = handler;
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        checkSongChange();
        updatePlaylistInfo();
        updateSongInfo();
        updateButtons();
    }
    
    private void updateButtons()
    {
        AudioPlayer player = AUDIO.getPlayer();
        if(player == null || !player.isPlaying() || player.isPaused())
            GUI.getGui().setPlayButton();
        else
            GUI.getGui().setPauseButton();
    }
    
    private void checkSongChange()
    {
        try{
            AudioPlayer player = AUDIO.getPlayer();
            if(player instanceof FileAudioPlayer && player.getPosition() == player.getDuration() && switchedIndex != player.getIndex())
            {
                switchedIndex = player.getIndex();
                player.changeSong(1);
            }
        }catch(Exception e){
        }
    }
    
    private void updateSongInfo()
    {
        try{
            AudioPlayer player = AUDIO.getPlayer();
            String time;
            if(player.isPlaying())
                time = minutes(player.getPosition()) + ":" + seconds(player.getPosition()) + "|" + minutes(player.getDuration()) + ":" + seconds(player.getDuration());
            else
                time = "";
            
            GUI.getGui().updateOverlay(time, player.getSongInfo(), player.getVolume());
            GUI.getGui().setTimeSliderPosition(player.getPosition(), player.getDuration());
        }catch(Exception e){
           // e.printStackTrace();
        }
    }
    
    private String seconds(int duration)
    {
        String str;
        int seconds = (int)(duration%60);
        if(seconds < 10)
        {
            str = "0"+seconds;
        }
        else
            str = seconds + "";
        return str;
    }
    
    private String minutes(int duration)
    {
        String str;
        int minutes = (int)(duration/60);
        if(minutes < 10)
        {
            str = "0"+minutes;
        }
        else
            str = minutes + "";
        return str;
    }
    
    private void  updatePlaylistInfo()
    {
        try{
            if(AUDIO.getPlayer() == null)
                return;
            if(playlist == null)
                playlist = AUDIO.getPlayer().getList();
            else if(playlist.equals(AUDIO.getPlayer().getList()))
                return;
            else
                playlist = AUDIO.getPlayer().getList();

            if(AUDIO.getPlayer() instanceof FileAudioPlayer)
                updateForFiles();
            else
                updateForYoutube();
        }catch(Exception e){
            
        }
    }
        
    private void updateForFiles()
    {
        try
        {
            ArrayList<MetaData> list = (ArrayList<MetaData>) AUDIO.getPlayer().getList();
            DefaultListModel songs = new DefaultListModel();
            DefaultListModel artists = new DefaultListModel();
            DefaultListModel albums = new DefaultListModel();
            
            for(int i = 0; i < list.size(); i++)
            {
                songs.addElement(list.get(i).getSongName());
                artists.addElement(list.get(i).getArtist());
                albums.addElement(list.get(i).getAlbum());
            }

            GUI.getGui().setPlaylistModels(songs, artists, albums);

            GUI.getGui().revalidate();
            GUI.getGui().repaint();
          //  if(GraphicalInterface.songlist.getSelectedIndex() != CowLiteAudioPlayer.player.getIndex())
           //     GraphicalInterface.songlist.setSelectedIndex(CowLiteAudioPlayer.player.getIndex());
        }catch(Exception f){}
    }
    
    private void updateForYoutube()
    {
        try
        {
            List<?> list = AUDIO.getPlayer().getList();
            DefaultListModel songs = new DefaultListModel();
            DefaultListModel artists = new DefaultListModel();
            DefaultListModel albums = new DefaultListModel();
            
            for(int i = 0; i < list.size(); i++)
            {
                songs.addElement(list.get(i));
            }

            GUI.getGui().setPlaylistModels(songs, artists, albums);
            GUI.getGui().revalidate();
            GUI.getGui().repaint();
            if(list.isEmpty())
                return;

           // GUIHandler.frame.repaint();
         //   GUIHandler.frame.revalidate();
         //   if(GraphicalInterface.songlist.getSelectedIndex() != CowLiteAudioPlayer.player.getIndex())
            //    GraphicalInterface.songlist.setSelectedIndex(CowLiteAudioPlayer.player.getIndex());
        }catch(Exception f){}
    }
}

