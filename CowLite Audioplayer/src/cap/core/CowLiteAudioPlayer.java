package cap.core;

import cap.control.GlobalKeyListener;
import cap.core.audio.AudioController;
import cap.gui.GUIHandler;
import java.io.File;
import java.io.PrintWriter;
import javafx.embed.swing.JFXPanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.jnativehook.GlobalScreen;

/**
 * (c) Copyright
 * This class initializes CowLite Audio Player. Main class, yippy!
 * @author Wessel Jongkind
 */
public class CowLiteAudioPlayer
{
    //private final CoreTime TIMER;

    /**
     * Everything starts here. Docpath is calculated, graphical interface handler
     * is started, the mediaplayer is located here.
     * @param args 
     */
    public static void main(String[] args)
    {
        try{
            //Save a buttload of RAM on some systems
            System.setProperty("sun.java2d.noddraw", "true");
            //Initialize the JFX thread (important, otherwise MediaPlayer in FileAudioPlayer won't work.
            new JFXPanel();

            new CowLiteAudioPlayer();
        }catch(Exception e){
            try{
                File f = new File("C:\\Users\\Wessel\\Documents\\log.txt");
                PrintWriter out = new PrintWriter(f);
                for(StackTraceElement s : e.getStackTrace())
                    out.println(s.toString());
                out.close();
            }catch(Exception f){
                
            }
        }
    }
    
    public CowLiteAudioPlayer() throws Exception
    {
        PropertiesManager properties = new PropertiesManager();
        System.out.println("Initial size" + properties.getPlaylists().size());
        System.out.println("Initial size" + properties.getPlaylists().keySet().size());
        AudioController audio = new AudioController(properties.getPlaylists(), properties.getAudioProperties());
        ApplicationController controller = new ApplicationController();
        GUIHandler handler = new GUIHandler(properties, audio, controller);
        controller.setAudioController(audio);
        controller.setGUI(handler);
        
        new Timer(1000, new CoreTime(handler, audio)).start();
        
        //Makes a global keylistener so that program is hotkey controlled.
        try{
            Logger l = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            l.setLevel(Level.OFF);
            l.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalKeyListener(properties.getControlProperties(), controller));
        }catch(Exception e){}

        
        
    }
}
