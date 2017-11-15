package cap.util;

import cap.core.CoreTime;
import cap.core.CowLiteAudioPlayer;
import cap.core.audio.FileAudioPlayer;
import cap.core.audio.MetaData;
import cap.gui.GUIHandler;
import cap.gui.GraphicalInterface;
import java.awt.Image;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * (c) Copyright
 * This class gets is pure utility. It makes jobs in other classes a whole lot
 * easier. Most of it's methods are static.
 * @author Wessel Jongkind
 */
public class InterfaceIO
{
    public static boolean pauseSet = true;
    
    /**
     * save a playlist file
     */
    public static void saveFile()
    {
        if(CowLiteAudioPlayer.player instanceof FileAudioPlayer)
        {
            GraphicalInterface.filechooser.showSaveDialog(GraphicalInterface.filechooser);

            try
            {
                String path = null;
                path = GraphicalInterface.filechooser.getSelectedFile().toURI().toURL().toString();
                if(path.contains("<Empty%20List>"))
                {
                    path = displayError();
                }
                //String path = MainFrame.filechooser.getSelectedFile().toURI().toURL().toString();
                path = path.replace("/", "\\\\");
                path = path.replace("file:\\\\", "");
                path = path.replace("%20", " ");
                PrintStream out1 = System.out;

                PrintStream out = new PrintStream(new FileOutputStream(new File(path)));
                System.setOut(out);

                ArrayList<MetaData> playlist = (ArrayList<MetaData>)CowLiteAudioPlayer.player.getList();
                for(int i = 0; i < playlist.size(); i++)
                {
                    System.out.println(playlist.get(i).getPath());
                }

                FileReader red = new FileReader(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt");
                BufferedReader bufred = new BufferedReader(red);
                String line = null;
                ArrayList<String> playlists = new ArrayList<>();
                playlists.add(path);
                CowLiteAudioPlayer.playlists.add(1, path);
                String empty = bufred.readLine();
                while((line = bufred.readLine()) != null)
                {
                    if(!line.equals(path))
                        playlists.add(line);
                }
                out = new PrintStream(new FileOutputStream(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt")));
                System.setOut(out);

                System.out.println(empty);
                for(int i = 0; i < playlists.size(); i++)
                {
                    System.out.println(playlists.get(i));
                }
                out.close();
                System.setOut(out1);
                CowLiteAudioPlayer.player.clearList();
                CowLiteAudioPlayer.player.loadList(path);
                GraphicalInterface.uptodate2 = false;
                CoreTime.update = true;
                playlists = null;
            }catch(Exception f){System.out.println(f + "savefile");}
        }
        else
        {
            //TODO
        }
    }
    
    private static String displayError()
    {
        JOptionPane.showMessageDialog(GUIHandler.frame,"Please select a valid save file.","File Error", JOptionPane.ERROR_MESSAGE);
        GraphicalInterface.filechooser.showSaveDialog(GraphicalInterface.filechooser);
        String path = null;
        try{
        if((path = GraphicalInterface.filechooser.getSelectedFile().toURI().toURL().toString()).contains("-Empty%20List-"))
            return displayError();
        else
            return path;
        }catch(Exception e){}
        return null;
    }
    
    /**
     * Remove a playlist file
     */
    public static void removeFile()
    {
        try
        {
            
            File f = new File(CowLiteAudioPlayer.playlists.get(GraphicalInterface.savedListText.getSelectedIndex()));
            if(f.toString().contains("<Empty List>"))
                return;
            while(f.exists())
                f.delete();
            
        }catch(Exception o){System.out.println(o + "IORemoveFile");}

        CowLiteAudioPlayer.playlists.remove(GraphicalInterface.savedListText.getSelectedIndex());
        try{
        GraphicalInterface.savedListText.remove(GraphicalInterface.savedListText.getSelectedIndex());
        CowLiteAudioPlayer.player.stop();
        CowLiteAudioPlayer.player.clearList();
        }catch(Exception e){}
        
        try
        {
            CowLiteAudioPlayer.player.loadList(CowLiteAudioPlayer.playlists.get(0));
        }catch(Exception f){}

        try
        {
            PrintStream out = new PrintStream(new FileOutputStream(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt")));
            System.setOut(out);

            for(int i = 0; i < CowLiteAudioPlayer.playlists.size(); i++)
            {
                System.out.println(CowLiteAudioPlayer.playlists.get(i));
            }
            out.close();
        }catch(Exception g){}
        setPlayButton();

        GraphicalInterface.uptodate = false;
        GraphicalInterface.uptodate2 = false;
        CoreTime.update = true;
    }
    
    /**
     * change the playbutton to have a play icon
     */
    public static void setPlayButton()
    {
        if(pauseSet)
        {
            pauseSet = false;
            try{
                Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\play.png"));
                ImageIcon icon = new ImageIcon(play.getScaledInstance(GraphicalInterface.SQUAREBUTTON - (GraphicalInterface.SQUAREBUTTON / 10),GraphicalInterface.SQUAREBUTTON - (GraphicalInterface.SQUAREBUTTON / 10),50));
                GraphicalInterface.playButton.setIcon(icon);
                play.flush();
                play = null;
                icon = null;
            }catch(Exception f){System.out.println(f + "setplaybutton");}
        }
    }
    
    /**
     * change the playbutton to have a pause button
     */
    public static void setPauseButton()
    {
        if(!pauseSet)
        {
            pauseSet = true;
            try{
                Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\pause.png"));
                ImageIcon icon = new ImageIcon(play.getScaledInstance(GraphicalInterface.SQUAREBUTTON - (GraphicalInterface.SQUAREBUTTON / 10),GraphicalInterface.SQUAREBUTTON - (GraphicalInterface.SQUAREBUTTON / 10),50));
                GraphicalInterface.playButton.setIcon(icon);
                play.flush();
                play = null;
                icon = null;
            }catch(Exception f){System.out.println(f);}
        }
    }
    
    /**
     * to show that the shufflefunction is on
     */
    public static void setShufflePressed()
    {
        try{
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\shufflepressed.png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance((int)(GraphicalInterface.RECTBUTTON / 0.75 - (GraphicalInterface.RECTBUTTON /0.75 / 10)),GraphicalInterface.RECTBUTTON - (GraphicalInterface.RECTBUTTON / 10),50));
            GraphicalInterface.shuffleButton.setIcon(icon);
            play.flush();
            play = null;
            icon = null;
        }catch(Exception f){System.out.println(f);}
    }
    
    /**
     * to show that the shufflefunction is off
     */
    public static void setShuffle()
    {
        try{
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\shuffle.png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance((int)(GraphicalInterface.RECTBUTTON / 0.75 - (GraphicalInterface.RECTBUTTON /0.75 / 10)),GraphicalInterface.RECTBUTTON - (GraphicalInterface.RECTBUTTON / 10),50));
            GraphicalInterface.shuffleButton.setIcon(icon);
            play.flush();
            play = null;
            icon = null;
        }catch(Exception f){System.out.println(f);}
    }
    
    public static void setAlphabeticPressed()
    {
        try{
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\AlphabeticEnabled.png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance((int)(GraphicalInterface.RECTBUTTON / 0.65 - (GraphicalInterface.RECTBUTTON /0.65 / 10)),GraphicalInterface.RECTBUTTON - (GraphicalInterface.RECTBUTTON / 10),50));
            GraphicalInterface.alphabeticButton.setIcon(icon);
            play.flush();
            play = null;
            icon = null;
        }catch(Exception f){System.out.println(f);}
    }
    
    public static void setAlphabetic()
    {
        try{
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\AlphabeticDisabled.png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance((int)(GraphicalInterface.RECTBUTTON / 0.65 - (GraphicalInterface.RECTBUTTON /0.65 / 10)),GraphicalInterface.RECTBUTTON - (GraphicalInterface.RECTBUTTON / 10),50));
            GraphicalInterface.alphabeticButton.setIcon(icon);
            play.flush();
            play = null;
            icon = null;
        }catch(Exception f){System.out.println(f);}
    }
}
