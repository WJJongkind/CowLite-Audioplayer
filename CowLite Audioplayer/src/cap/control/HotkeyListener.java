package cap.control;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.jnativehook.keyboard.*;

/**
 * (c) Copyright This class makes the whole hotkey-thing possible. Thanks to
 * this class we can still control the program from outside of the interface.
 */
public class HotkeyListener implements NativeKeyListener {
    
    // MARK: - Associated types & constants
    
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
    
    public interface HotkeyListenerDelegate {
        public void didPressPlay();
        public void didPressPause();
        public void didPressStop();
        public void didPressPrevious();
        public void didPressNext();
        public void didPressVolumeUp();
        public void didPressVolumeDown();
        public void repositionOverlay(int dx, int dy);
        public void allowOverlayRepositioning();
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
            delegate.allowOverlayRepositioning();
            repositionIfNeeded();
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

    /**
     * checks if any key gets released from any window
     *
     * @param e the key
     */
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
