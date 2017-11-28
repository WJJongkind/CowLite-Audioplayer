package cap.control;

import cap.core.ApplicationController;
import cap.core.CowLiteAudioPlayer;
import cap.core.audio.AudioPlayer;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Map;
import org.jnativehook.keyboard.*;

/**
 * (c) Copyright
 * This class makes the whole hotkey-thing possible. Thanks to this class we can
 * still control the program from outside of the interface.
 */
public class GlobalKeyListener implements NativeKeyListener
{
    public static String lastPressed;
    private final Map<String, String> CONTROLS;
    public static boolean settingsOn = false;
    public static boolean left, right, top, bottom, show, rotateText, rotateBack;
    public static boolean alt;
    private int repositionCounter = 0;
    private final int TRESHOLD = 30, TRESHOLD2 = 60;
    private final ApplicationController CONTROLLER;
    
    /**
     * initializes the global key listener
     */
    public GlobalKeyListener(Map<String, String> controls, ApplicationController controller)
    {
        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
        CONTROLS = controls;
        CONTROLLER = controller;
    }
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e)
    {
        if(e.getKeyCode() == NativeKeyEvent.VC_ALT_L)
            this.alt = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_LEFT)
            this.left = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_RIGHT)
            this.right = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_UP)
            this.top = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_DOWN)
            this.bottom = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_S)
            this.show = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_T)
            this.rotateText = true;
        if(e.getKeyCode() == NativeKeyEvent.VC_B)
            this.rotateBack = true;
        if(alt)
        {
            reposition();
            toggleOverlay();
        }
    }
    
    private void toggleOverlay()
    {
        if(alt && show)
            CONTROLLER.toggleOverlay();
    }
    
    private void reposition()
    {
        if(alt)
        {
            int speed = 1;
            if(repositionCounter > TRESHOLD)
                speed = 2;
            if(repositionCounter > TRESHOLD2)
                speed = 4;
            
            int x = 0;
            int y = 0;
            
            if(left)
                x = -1*speed;
            if(right)
                x = speed;
            if(top)
                y = -1*speed;
            if(bottom)
                y = speed;
            
            if(x != 0 || y != 0)
                repositionCounter++;
            else
                repositionCounter = 0;
            CONTROLLER.repositionOverlay(x, y);
        }
    }

    /**
     * checks if any key gets released from any window
     * @param e the key
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e)
    {
        if(e.getKeyCode() == NativeKeyEvent.VC_ALT_L)
            this.alt = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_LEFT)
            this.left = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_RIGHT)
            this.right = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_UP)
            this.top = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_DOWN)
            this.bottom = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_S)
            this.show = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_T)
            this.rotateText = false;
        if(e.getKeyCode() == NativeKeyEvent.VC_B)
            this.rotateBack = false;
        
        if(!left && !right && !top && !bottom)
            repositionCounter = 0;
        
        if(!settingsOn)
        {
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("play")))
                CONTROLLER.playEvent();
            
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("stop")))
                CONTROLLER.stopEvent();
            
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("previous")))
                CONTROLLER.previousSongEvent();
            
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("next")))
                CONTROLLER.nextSongEvent();
                
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("pause")))
                CONTROLLER.pauseEvent();
            
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("volumeDown")))
                CONTROLLER.changeVolumeEvent(-5);
            
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(CONTROLS.get("volumeUp")))
                CONTROLLER.changeVolumeEvent(5);
        }
        else
        {
            lastPressed = NativeKeyEvent.getKeyText(e.getKeyCode());
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}


