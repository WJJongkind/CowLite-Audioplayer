package cap.control;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.jnativehook.keyboard.*;

/**
 * This class makes the whole hotkey-thing possible. Thanks to
 * this class we can still control the program from outside of the user interface.
 */
public class HotkeyListener implements NativeKeyListener {
    
    // MARK: - Associated types & constants
    
    /**
     * The various controls that the hotkeylistener can hook into
     */
    public enum Control {
        play("play"),
        pause("pause"),
        stop("stop"),
        previousSong("previous"),
        nextSong("next"),
        volumeUp("volumeUp"),
        volumeDown("volumeDown");
        
        public String rawValue;
        
        private Control(String rawValue) {
            this.rawValue = rawValue;
        }
    
        // MARK: - Lookup
    
        public static final HashMap<String, Control> lookup = new HashMap<>();
        
        static {
            lookup.put(play.rawValue, play);
            lookup.put(pause.rawValue, pause);
            lookup.put(stop.rawValue, stop);
            lookup.put(previousSong.rawValue, previousSong);
            lookup.put(nextSong.rawValue, nextSong);
            lookup.put(volumeUp.rawValue, volumeUp);
            lookup.put(volumeDown.rawValue, volumeDown);
        }
        
    }
    
    /**
     * Interface for delegates that want to act whenever a specific hotkey is pressed.
     */
    public interface HotkeyListenerDelegate {
        
        /**
         * Notifies the delegate that the "play" hotkey was pressed.
         */
        public void didPressPlay();
        
        /**
         * Notifies the delegate that the "pause" hotkey was pressed.
         */
        public void didPressPause();
        
        /**
         * Notifies the delegate that the "stop" hotkey was pressed.
         */
        public void didPressStop();
        
        /**
         * Notifies the delegate that the "play previous song" hotkey was pressed.
         */
        public void didPressPrevious();
        
        /**
         * Notifies the delegate that the "play next song" hotkey was pressed.
         */
        public void didPressNext();
        
        /**
         * Notifies the delegate that the "volume up" hotkey was pressed.
         */
        public void didPressVolumeUp();
        
        /**
         * Notifies the delegate that the "volume down" hotkey was pressed.
         */
        public void didPressVolumeDown();
        
        /**
         * Notifies the delegate that the "reposition overlay" hotkey combination was pressed. The delegate should ensure that, if enabled,
         * the overlay should be repositioned in the given direction.
         * @param dx The amount of pixels in the x-axis that the overlay should be moved.
         * @param dy The amount of pixels in the y-axis that the overlay should be moved.
         */
        public void repositionOverlay(int dx, int dy);
        
        /**
         * Notifies the delegate that overlay repositioning should be allowed or disallowed.
         * @param shouldAllowOverlayRepositioning True if repositioning should be allowed, false otherwise.
         */
        public void shouldAllowOverlayRepositioning(boolean shouldAllowOverlayRepositioning);
        
        /**
         * Notifies the delegate that the overlay should be toggled (turned off if it is on, turned on if it is off).
         */
        public void toggleOverlay();
        
    }
    
    // MARK: - Private final properties
    
    private Map<Control, String> controls;
    
    // MARK: - Private variables
    
    private boolean settingsOn = false;
    private boolean left, right, top, bottom, show, alt;
    private WeakReference<HotkeyListenerDelegate> delegate = new WeakReference<>(null);

    // MARK: - Initialisers
    
    public HotkeyListener(Map<Control, String> controls) {
        this.controls = controls;
    }
    
    // MARK: - Getters & Setters
    
    public void setDelegate(HotkeyListenerDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public Map<Control, String> getControls() {
        return controls;
    }
    
    public void setControls(Map<Control, String> controls) {
        this.controls = controls;
    }
    
    // MARK: - NativeKeyListener

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L) {
            this.alt = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
            this.left = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
            this.right = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_UP) {
            this.top = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
            this.bottom = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_S) {
            this.show = true;
        }
        
        HotkeyListenerDelegate delegate = this.delegate.get();
        if (alt) {
            delegate.shouldAllowOverlayRepositioning(true);
            repositionIfNeeded();
        } else {
            delegate.shouldAllowOverlayRepositioning(false);
        }
        
        if(alt && show) {
            
            if(delegate != null) {
                delegate.toggleOverlay();
            }
        }
    }

    private void repositionIfNeeded() {
        if (alt) {
            int x = 0;
            int y = 0;

            if (left) {
                x = -1;
            }
            if (right) {
                x = 1;
            }
            if (top) {
                y = -1;
            }
            if (bottom) {
                y = 1;
            }
            
            HotkeyListenerDelegate delegate = this.delegate.get();
            if(delegate != null) {
                delegate.repositionOverlay(x, y);
            }
        }
    }
    
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L) {
            this.alt = false;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
            this.left = false;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
            this.right = false;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_UP) {
            this.top = false;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
            this.bottom = false;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_S) {
            this.show = false;
        }

        HotkeyListenerDelegate delegate = this.delegate.get();
        if(delegate != null) {
            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.play))) {
                delegate.didPressPlay();
            }

            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.stop))) {
                delegate.didPressStop();
            }

            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.pause))) {
                delegate.didPressPause();
            }

            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.previousSong))) {
                delegate.didPressPrevious();
            }

            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.nextSong))) {
                delegate.didPressNext();
            }

            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.volumeDown))) {
                delegate.didPressVolumeDown();
            }

            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(controls.get(Control.volumeUp))) {
                delegate.didPressVolumeUp();
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
    
}
