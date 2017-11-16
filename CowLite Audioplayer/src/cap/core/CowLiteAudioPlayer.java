package cap.core;

import cap.control.GlobalKeyListener;
import cap.core.audio.AudioPlayer;
import cap.core.audio.FileAudioPlayer;
import cap.core.audio.youtube.YTAudioPlayer;
import cap.gui.GUIHandler;
import cap.gui.GraphicalInterface;
import java.awt.Color;
import java.io.*;
import javafx.embed.swing.JFXPanel;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;
import org.jnativehook.GlobalScreen;

/**
 * (c) Copyright
 * This class initializes CowLite Audio Player. Main class, yippy!
 * @author Wessel Jongkind
 */
public class CowLiteAudioPlayer
{
    public static AudioPlayer player;
    public static Timer timer;
    private static ServerSocket socket;
    public static String docPath;
    public static ArrayList<String> playlists = new ArrayList<>();
    public static GUIHandler gui;
    public static ArrayList<Color> colorindex;

    /**
     * Everything starts here. Docpath is calculated, graphical interface handler
     * is started, the mediaplayer is located here.
     * @param args 
     */
    public static void main(String[] args) 
    {
        //Save a buttload of RAM on some systems
        System.setProperty("sun.java2d.noddraw", "true");
        
        //Obtains the user's documents path
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        try{ docPath = fw.getDefaultDirectory().toURI().toURL().toString();}catch(Exception e){}
        
        docPath = docPath.replace("/", "\\\\");
        docPath = docPath.replace("file:\\\\", "");
        
        //Obtains all the saved playlists
        try{
            FileReader red = new FileReader(docPath + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt");
            BufferedReader bufred = new BufferedReader(red);
            String line = null;
            
            while((line = bufred.readLine()) != null)
            {
                if(!line.contains("||"))
                    playlists.add(line);
                else
                    playlists.add(line.split("\\|\\|")[1]);
            }
            red.close();
            bufred.close();
        }catch(Exception e){}
        
        try
        {
            //Creates a new socket. Only one instance of this program is allowed to run at a time.
            socket = new ServerSocket(9999,0,InetAddress.getByAddress(new byte[] {127,0,0,1}));
        
            //retreive all graphical settings
            getGraphics(docPath + "CowLite Audio Player\\resources\\launchersettings\\GraphicsSettings.txt");
            
            //retreive all window settings and make a new GUI with those settings
            FileReader red = new FileReader(docPath + "CowLite Audio Player\\resources\\launchersettings\\WindowSettings.txt");
            BufferedReader bufred = new BufferedReader(red);
            String line = null;
            bufred.readLine();
            
            //Makes a global keylistener so that program is hotkey controlled.
            try{
                Logger l = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                l.setLevel(Level.OFF);
                l.setUseParentHandlers(false);
                GlobalScreen.registerNativeHook();
                GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
            }catch(Exception e){}
            
            gui = new GUIHandler(Integer.parseInt(bufred.readLine()),Integer.parseInt(bufred.readLine()),Integer.parseInt(bufred.readLine()),Integer.parseInt(bufred.readLine()));
            
            red.close();
            bufred.close();

            //Initialize the JFX thread (important, otherwise MediaPlayer in AudioPlayer won't work.
            JFXPanel pane = new JFXPanel();
            pane = null;

            //Create the audioplayer
            player = new FileAudioPlayer();
           // player = new YTAudioPlayer();
           // player.loadList("PL7aXwAD1wk5kdkuQOFBHY63R9Bhki7rCg");
            
            //Makes sure the frame gets refreshed
            GraphicalInterface.uptodate2 = false;
            timer = new Timer(1000, new CoreTime());
            timer.start();
            
            /*For if this program gets launched with one or more filepaths as an argument.
              It would be awesome if someone could write a decent
              launcher.*/
            /*if(args.length > 0)
            {
                for(int i = 0; i < args.length; i++)
                {
                    player.addSong(args[i]);
                }
                player.play();
                GraphicalInterface.uptodate = false;
            }
            System.out.println("done3");*/
            
            CoreTime.update = true;
        }catch(Exception g){g.printStackTrace();}
    }
    
    public static void loadList(String list)
    {
        player.stop();
        File file = new File(list);
        if(file.exists())
        {
            player = new FileAudioPlayer();
            player.loadList(list);
        }
        else
        {
            player = new YTAudioPlayer();
            player.loadList(list);
        }
    }
    
    private static void getGraphics(String path)
    {
        try{
            //Gets all the graphics values from the settings file. Uses
            //regex to see what the settings are.
            colorindex = new ArrayList<>();
            String patternString = "java.awt.Color\\[r=(\\d*),g=(\\d*),b=(\\d*)\\]";
            Pattern pattern = Pattern.compile(patternString);
            System.out.println("done");
            Matcher matcher;
            int i = 0;
            String line = null;
            FileReader red = new FileReader(new File(path));
            BufferedReader bufred = new BufferedReader(red);
            System.out.println("done");
            bufred.readLine();
            while((line = bufred.readLine()) != null)
            {
                matcher = pattern.matcher(line);
                if(matcher.matches())
                {
                    colorindex.add(new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
                    i++;
                }
            }
        }catch(Exception e){System.out.println(e + "getgraphics");}
    }
}
