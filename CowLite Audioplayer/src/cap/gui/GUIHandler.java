package cap.gui;

import cap.core.audio.FileAudioPlayer;
import cap.core.CoreTime;
import cap.core.CowLiteAudioPlayer;
import cap.gui.overlay.TranslucentFrame;
import cap.core.DragDropListener;
import cap.gui.overlay.InfoComponent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.io.*;
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
    public static JFrame frame;
    private TranslucentFrame tf;

    /**
     * Creates the interface and it's listeners
     * @param x coordinate x on screen
     * @param y coordinate y on screen
     * @param w width of the interface
     * @param h height of the interface
     */
    public GUIHandler(int x, int y, int w, int h)
    {
        //makes a new main GUI
        frame = new GraphicalInterface("CowLite Media Player");

        frame.setLocation(x,y);

        //minimum width and height are 579x264 for current version. Below that
        //it starts glitching out.
        if(w >= 579 && h >= 264)
            frame.setSize(w, h);
        else
            frame.pack();

        //Should probably be done in MainFrame
        GraphicalInterface.oldDimension = new Dimension(frame.getWidth(), frame.getHeight());
        GraphicalInterface.oldPoint = new Point(frame.getLocation());

        //Default close operation, adds ActionListener which handles
        //all the button events
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowFocusListener(new GUIListener());

        //If closes all the settings should be stored
        frame.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent)
            {
                File f = new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\" + "Launched.txt");
                f.delete();
                int i = 0;
                f = new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\arguments\\arg" + i + ".txt");

                while(i <= CoreTime.imax)
                {
                    i++;
                    f.delete();
                    f = new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\arguments\\arg" + i + ".txt");
                }

                try
                {
                    PrintStream out = new PrintStream(new FileOutputStream(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\LaunchSettings.txt")));
                    System.setOut(out);
                    System.out.println("settings");
                    System.out.println(CowLiteAudioPlayer.player.getVolume());
                    System.out.println(CowLiteAudioPlayer.player.getShuffled());
                    System.out.println(CowLiteAudioPlayer.player.getAlphabetical());
                    System.out.println(CowLiteAudioPlayer.player.getAlphabeticalType());

                    PrintStream out2 = new PrintStream(new FileOutputStream(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\WindowSettings.txt")));
                    System.setOut(out2);
                    System.out.println("Window Settings");
                    System.out.println((int)GraphicalInterface.oldPoint.getX());
                    System.out.println((int)GraphicalInterface.oldPoint.getY());
                    System.out.println((int)GraphicalInterface.oldDimension.getWidth());
                    System.out.println((int)GraphicalInterface.oldDimension.getHeight());

                    PrintStream out3 = new PrintStream(new FileOutputStream(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\launchersettings\\GraphicsSettings.txt")));
                    System.setOut(out3);
                    System.out.println("Background, listbg, listtext, menutext, sliderbackprim, sliderbacksec, volumeprim, volumesec, timeprim, timesec, time");
                    System.out.println(GraphicalInterface.BACKGROUND);
                    System.out.println(GraphicalInterface.LISTBG);
                    System.out.println(GraphicalInterface.PLAYLISTTEXT);
                    System.out.println(GraphicalInterface.MENUTEXTCOLOR);
                    System.out.println(VolumeSlider.backColors[0]);
                    System.out.println(VolumeSlider.backColors[1]);
                    System.out.println(VolumeSlider.fillColors[0]);
                    System.out.println(VolumeSlider.fillColors[1]);
                    System.out.println(TimeSlider.fillColors[0]);
                    System.out.println(TimeSlider.fillColors[1]);
                    System.out.println(TimeSlider.timecolor);
                    out.close();
                }catch(Exception e){}

                System.exit(0);
            }
        });     

        //Droptarget so that users can drag n drop songs
        new DropTarget(frame, new DragDropListener());

        //CowLite logo.
        try{
            ImageIcon image = new ImageIcon(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\Cow32.png");
            frame.setIconImage(image.getImage());
            image = null;
        }catch(Exception e){System.out.println(e + "window closing");}
        frame.setVisible(true);
        GraphicalInterface theFrame = (GraphicalInterface) frame;
        theFrame.orderDividers();
        
        initInfo();
    }
    
    private void initInfo()
    {
        tf = new TranslucentFrame();
        tf.setAlwaysOnTop(true);
        
        try{
            BufferedReader red = new BufferedReader(new FileReader(CowLiteAudioPlayer.docPath + "\\CowLite Audio Player\\resources\\launchersettings\\overlay.txt"));
            red.readLine();

            if(Boolean.parseBoolean(red.readLine()))
                tf.setVisible(true);
        }catch(Exception e){}
    }
    
    public void updateOverlay(String time, String song, double volume)
    {
        InfoComponent info = tf.getInfoComponent();
        info.setSong(song);
        info.setTime(time);
        info.setVolume(volume);
        tf.repaint();
    }
    
    public void repositionOverlay(int addX, int addY)
    {
        InfoComponent info = tf.getInfoComponent();
        
        info.changeOffsetX(addX);
        info.changeOffsetY(addY);
        info.repaint();
    }
    
    public void toggleOverlay()
    {
        tf.setVisible(!tf.isVisible());
        if(tf.isVisible())
            tf.getInfoComponent().setOffsetX(0);
            tf.getInfoComponent().setOffsetY(0);
    }
    
    public void toggleOverlay(boolean visible)
    {
        tf.setVisible(visible);
    }
    
    public InfoComponent getOverlay()
    {
        return tf.getInfoComponent();
    }
    
    public void setOverlaySettings(String size)
    {
        tf.getInfoComponent().setActiveSizes(size);
    }
}
