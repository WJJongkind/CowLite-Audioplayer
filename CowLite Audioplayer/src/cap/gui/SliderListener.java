package cap.gui;

import cap.core.CowLiteAudioPlayer;
import javafx.util.Duration;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * (c) Copyright
 * This class gets controls the volumeslider & timebar and makes sure they
 * have a function.
 */
public class SliderListener implements ChangeListener
{
    public static boolean click = false;
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        try{
        if(e.getSource() == GraphicalInterface.volumeSlider)
            CowLiteAudioPlayer.player.setVolume(GraphicalInterface.volumeSlider.getValue());
        if((e.getSource() == GraphicalInterface.timeSlider && GraphicalInterface.timeSlider.getValue() != (int)CowLiteAudioPlayer.player.getPosition()) && click)
        {
            System.out.println(1000*GraphicalInterface.timeSlider.getValue());
            CowLiteAudioPlayer.player.seek(GraphicalInterface.timeSlider.getValue());
            click = false;
            return;
        }
            click = true;
        }catch(Exception f){f.printStackTrace();}
    }
    
}
