package cap.gui;

import cap.core.ApplicationController;
import cap.gui.overlay.TranslucentFrame;
import cap.core.DragDropListener;
import cap.core.PropertiesManager;
import cap.core.audio.AudioController;
import cap.gui.overlay.InfoComponent;
import cap.util.IO;
import java.awt.dnd.DropTarget;
import java.io.*;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * (c) Copyright
 * This class gets instantiated when the user is playing a video. Please do
 * note this: THE PROGRAM IS NOT AIMED AT VIDEOPLAYING, THIS IS A PURE EXTRA
 * Needs a lot of work.
 * @author Wessel Jongkind
 */
public class GUIHandler 
{
    private final GraphicalInterface frame;

    /**
     * Creates the interface and it's listeners
     * @param x coordinate x on screen
     * @param y coordinate y on screen
     * @param w width of the interface
     * @param h height of the interface
     */
    public GUIHandler(PropertiesManager properties, AudioController audio, ApplicationController controller) throws Exception
    {
        Map<String, Object> graphics = properties.getGraphicsProperties();
        
        //makes a new main GUI
        frame = new GraphicalInterface("CowLite Audioplayer", graphics, audio, properties, controller);

        frame.setLocation((int)graphics.get("x"),(int)graphics.get("y"));

        //minimum width and height are 579x264 for current version. Below that
        //it starts glitching out.
        int width = (int) graphics.get("width");
        int height = (int) graphics.get("height");
        
        if(width >= 579 && height >= 264)
            ((GraphicalInterface) frame).setInitialSize(width, height);
        else
            frame.pack();

        //Default close operation, adds ActionListener which handles
        //all the button events
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //If closes all the settings should be stored
        frame.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent)
            {
                try
                {
                    properties.storeProperties(properties.getGraphicsProperties());
                    properties.storeProperties(properties.getAudioProperties());
                    properties.storeProperties(properties.getControlProperties());
                }catch(Exception e){}

                System.exit(0);
            }
        });     

        //Droptarget so that users can drag n drop songs
        new DropTarget(frame, new DragDropListener(audio));

        //CowLite logo.
        try{
            ImageIcon image = new ImageIcon(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\Cow32.png");
            frame.setIconImage(image.getImage());
            image = null;
        }catch(Exception e){System.out.println(e + "window closing");}
        frame.setVisible(true);
        GraphicalInterface theFrame = (GraphicalInterface) frame;
        theFrame.orderDividers();
    }
    
    public GraphicalInterface getGui()
    {
        return frame;
    }
}
