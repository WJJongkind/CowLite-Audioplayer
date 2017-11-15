package cap.core;

import static cap.core.CoreTime.update;
import cap.core.audio.FileAudioPlayer;
import cap.core.audio.MetaData;
import cap.gui.GUIHandler;
import static cap.gui.GraphicalInterface.songlistmodel;
import static cap.gui.GraphicalInterface.artistlistmodel;
import static cap.gui.GraphicalInterface.albumlistmodel;
import cap.gui.GUIListener;
import cap.gui.GraphicalInterface;
import cap.gui.SliderListener;
import java.awt.event.ActionListener;
import java.io.*;
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
    public static int imax = 0;
    public static boolean update = true;
    private File f;
    
    public CoreTime()
    {
        f = new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player" + "\\resources\\arguments\\arg" + imax + ".txt");
    }
        
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        System.out.println("action");
        try{
            updateFrame();
            CowLiteAudioPlayer.gui.updateOverlay(GraphicalInterface.time, CowLiteAudioPlayer.player.getSongInfo(), CowLiteAudioPlayer.player.getVolume());
            checkSongChange();
            updateTime();
        }catch(Exception f){f.printStackTrace();}
        
        //Checks if song should change and if new files have arrived in CowLite's
        //directory. If so, those files will be opened.
        //checkNewFiles();
    }
    
    /*private void checkNewFiles()
    {
        try
        {
            //checks if file 'arg + i + .txt' already exists. Those get
            //created by launcher.exe (a seperate program I wrote)
            if(f.exists());
            {
                int i=imax;
                while(f.exists())
                {
                    try
                    {
                        i++;
                        interpretFile(f.getAbsolutePath());
                        f = new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player" + "\\resources\\arguments\\arg" + i + ".txt");
                    }catch(Exception e){}
                }
                imax = i;
            }
        }catch(Exception g){}
    }
    
    private void interpretFile(String path)
    {
        String line = null;
        try
        {
            //Retreives the filepath from the file with filepath path.
            FileReader red = new FileReader(path);
            BufferedReader bufred = new BufferedReader(red);
            while((line = bufred.readLine()) != null)
            {
                CowLiteAudioPlayer.player.addSong(line);
            }
            red.close();
            bufred.close();
        }catch(Exception e){}
        line = null;
    }*/
    
    private void checkSongChange()
    {
        try{
            if(CowLiteAudioPlayer.player.isFinished())
            {
                //If we're in here, the song's current position is at it's end.
                //Player gets stopped, song gets changed, player gets started. 
                CowLiteAudioPlayer.player.stop();
                CowLiteAudioPlayer.player.changeSong(1);
                CowLiteAudioPlayer.player.play();
                SliderListener.click = false;
                GraphicalInterface.timeSlider.setMaximum(-1);
                
            }
            GraphicalInterface.songlist.setSelectedIndex(CowLiteAudioPlayer.player.getIndex());
        }catch(Exception g){g.printStackTrace();} 
    }
    
    private void updateTime()
    {
        System.out.println("Checking time...");
            try{
                //The timeslider gets updated on the interface.
                SliderListener.click = false;
                //Duration currentDuration = CowLiteAudioPlayer.player.;
                //Duration songDuration = mediaplayer.getMedia().getDuration();
                
                int duration = CowLiteAudioPlayer.player.getDuration();
                int position = CowLiteAudioPlayer.player.getPosition();
                if(GraphicalInterface.timeSlider.getMaximum() != duration || GraphicalInterface.timeSlider.getValue() == -1)
                    GraphicalInterface.timeSlider.setMaximum(duration);
                GraphicalInterface.timeSlider.setValue(position);
                
                //Crafts a m'm':s's' | mm:ss string which will be displayed
                //on top of the timeslider.
                String totalTime = minutes(duration) + ":" + seconds(duration);
                String currentTime = minutes(position) + ":" + seconds(position);
                GraphicalInterface.time = currentTime + "|" + totalTime;
            }catch(Exception e){
                SliderListener.click = true;
                e.printStackTrace();
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
    
    private void  updateFrame() throws Exception
    {
        //updates the MainFrame if playlists have changed in AudioPlayer class.
        if(!GraphicalInterface.uptodate)
        {
            if(CowLiteAudioPlayer.player instanceof FileAudioPlayer)
                updateForFiles();
            else
                updateForYoutube();
        }
        
        //Updates the mainframe if a completely new playlist has been saved
        if(!GraphicalInterface.uptodate2)
        {
            GraphicalInterface.savedmodel = new DefaultListModel();
            try
            {
                FileReader red = new FileReader(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt");
                BufferedReader bufred = new BufferedReader(red);
                String line = null;
                
                while((line = bufred.readLine()) != null)
                {
                    while(line.contains("\\"))
                    {
                        line = line.substring(line.indexOf("\\") + 1);
                    }
                    GraphicalInterface.savedmodel.addElement(line);
                }
                red.close();
                bufred.close();
                GraphicalInterface.savedListText.setModel(GraphicalInterface.savedmodel);
                GraphicalInterface.uptodate2 = true;
            }catch(Exception f){f.printStackTrace();}
        }
        
        if(!GUIListener.needupdate)
            update = false;
    }
        
    private void updateForFiles()
    {
        ArrayList<MetaData> list = (ArrayList<MetaData>) CowLiteAudioPlayer.player.getList();
        songlistmodel = new DefaultListModel();
        artistlistmodel = new DefaultListModel();
        albumlistmodel = new DefaultListModel();
        try
        {
            for(int i = 0; i < list.size(); i++)
            {
                songlistmodel.addElement(list.get(i).getSongName());
                artistlistmodel.addElement(list.get(i).getArtist());
                albumlistmodel.addElement(list.get(i).getAlbum());
            }

            GraphicalInterface.songlist.setModel(songlistmodel);
            GraphicalInterface.artistlist.setModel(artistlistmodel);
            GraphicalInterface.albumlist.setModel(albumlistmodel);

            GUIHandler.frame.repaint();
            GUIHandler.frame.revalidate();
            if(GraphicalInterface.songlist.getSelectedIndex() != CowLiteAudioPlayer.player.getIndex())
                GraphicalInterface.songlist.setSelectedIndex(CowLiteAudioPlayer.player.getIndex());
            GraphicalInterface.uptodate = true;
        }catch(Exception f){f.printStackTrace();}
    }
    
    private void updateForYoutube()
    {
        List<?> list = CowLiteAudioPlayer.player.getList();
        songlistmodel = new DefaultListModel();
        artistlistmodel = new DefaultListModel();
        albumlistmodel = new DefaultListModel();
        try
        {
            for(int i = 0; i < list.size(); i++)
            {
                songlistmodel.addElement(list.get(i));
            }

            GraphicalInterface.songlist.setModel(songlistmodel);
            GraphicalInterface.artistlist.setModel(artistlistmodel);
            GraphicalInterface.albumlist.setModel(albumlistmodel);
            if(list.isEmpty())
                return;

            GUIHandler.frame.repaint();
            GUIHandler.frame.revalidate();
            if(GraphicalInterface.songlist.getSelectedIndex() != CowLiteAudioPlayer.player.getIndex())
                GraphicalInterface.songlist.setSelectedIndex(CowLiteAudioPlayer.player.getIndex());
            System.out.println("updated");
        }catch(Exception f){f.printStackTrace();}
    }
}

