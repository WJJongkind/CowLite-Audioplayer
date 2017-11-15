package cap.control;

import cap.core.CowLiteAudioPlayer;
import static cap.core.CowLiteAudioPlayer.player;
import cap.gui.GraphicalInterface;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.jnativehook.keyboard.*;

/**
 * (c) Copyright
 * This class makes the whole hotkey-thing possible. Thanks to this class we can
 * still control the program from outside of the interface.
 */
public class GlobalKeyListener implements NativeKeyListener
{
    public static String play;
    public static String pause;
    public static String stop;
    public static String volumeup;
    public static String volumedown;
    public static String next;
    public static String previous;
    public static String lastpressed;
    public static boolean settingsOn = false;
    private boolean left, right, top, bottom, show, rotateText, rotateBack;
    public static boolean alt;
    private int repositionCounter = 0;
    public static final int TRESHOLD = 30, TRESHOLD2 = 60;
    /**
     * initializes the global key listener
     */
    public GlobalKeyListener()
    {
        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
        String line = null;
        ArrayList<String> settings = new ArrayList<>();
        
        try{
            FileReader red = new FileReader(CowLiteAudioPlayer.docPath + "\\CowLite Audio Player\\resources\\launchersettings\\controls.txt");
            BufferedReader bufred = new BufferedReader(red);
            while((line = bufred.readLine()) != null)
            {
                settings.add(line);
            }
            
            play = settings.get(1);
            pause = settings.get(2);
            stop = settings.get(3);
            volumeup = settings.get(4);
            volumedown = settings.get(5);
            next = settings.get(6);
            previous = settings.get(7);
            settings = null;
        }catch(Exception e){System.out.println(e + "GlobalKeyListener");}
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
            setOverlayColors();
        }
    }
    
    private void setOverlayColors()
    {
        if(alt)
        {
            if(rotateText)
                CowLiteAudioPlayer.gui.getOverlay().rotateTextColor();
            if(rotateBack)
                CowLiteAudioPlayer.gui.getOverlay().toggleOverlayBackground();
            CowLiteAudioPlayer.gui.getOverlay().repaint();
        }
    }
    
    private void toggleOverlay()
    {
        if(alt && show)
            CowLiteAudioPlayer.gui.toggleOverlay();
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
            CowLiteAudioPlayer.gui.repositionOverlay(x, y);
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
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(play))
            {
               if(GraphicalInterface.savedListText.getSelectedIndex() == -1 && GraphicalInterface.songlist.getSelectedIndex() == -1)
               {
                   if(CowLiteAudioPlayer.player.getList() == null || CowLiteAudioPlayer.player.getList().size() == 0)
                   {
                       try{
                           CowLiteAudioPlayer.player.loadList(CowLiteAudioPlayer.playlists.get(1));
                           CowLiteAudioPlayer.player.play();
                       }catch(Exception f){}
                       return;
                   }
               }
                if(player.isPlaying())
                {
                    if(player.isPaused())
                        player.setPaused(false);
                    else
                        player.setPaused(true);
                }
                else
                    player.play();
            }
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(stop))
            {
                CowLiteAudioPlayer.player.stop();
                return;
            }
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(previous))
            {
                CowLiteAudioPlayer.player.stop();
                CowLiteAudioPlayer.player.changeSong(-1);
                CowLiteAudioPlayer.player.play();
                return;
            }
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(next))
            {
                CowLiteAudioPlayer.player.stop();
                CowLiteAudioPlayer.player.changeSong(1);
                CowLiteAudioPlayer.player.play();
                GraphicalInterface.songlist.setSelectedIndex(CowLiteAudioPlayer.player.getIndex());
                return;
            }
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(pause))
            {
                if(player.isPlaying())
                {
                    if(player.isPaused())
                        player.setPaused(false);
                    else
                        player.setPaused(true);
                }
                else
                    player.play();
            }
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(volumedown))
            {
                CowLiteAudioPlayer.player.changeVolume(-5);
            }
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(volumeup))
            {
                CowLiteAudioPlayer.player.changeVolume(5);
            }
        }
        else
        {
            lastpressed = NativeKeyEvent.getKeyText(e.getKeyCode());
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}


