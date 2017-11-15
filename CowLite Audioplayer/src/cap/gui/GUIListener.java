package cap.gui;

import cap.core.CoreTime;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**
 * (c) Copyright
 * This class sets the timebar visible/invisible. It needend be visible for
 * when the user is not looking at the audioplayer interface. Saves CPU.
 */
public class GUIListener implements WindowFocusListener
{
    public static boolean needupdate = true;

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        needupdate = true;
        CoreTime.update = needupdate;
        GraphicalInterface.timeSlider.setVisible(true);
    }

    @Override
    public void windowLostFocus(WindowEvent e)
    {
        needupdate = false;
        CoreTime.update = needupdate;
        GraphicalInterface.timeSlider.setVisible(false);
    }
    
}
