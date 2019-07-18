package cap.core;

import cap.control.HotkeyListener;
import cap.control.HotkeyListener.Control;
import cap.gui.MainWindow;
import cap.gui.Window;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import com.sun.jna.NativeLibrary;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import uk.co.caprica.vlcj.binding.RuntimeUtil;

/**
 * (c) Copyright This class initializes CowLite Audio Player. Main class, yippy!
 *
 * @author Wessel Jongkind
 */
public class CowLiteAudioPlayer {
    
    // MARK: - Constants
    
    private static final HashMap<Control, String> controls = new HashMap<>();
    
    static {
        controls.put(Control.play, "NumPad 5");
        controls.put(Control.pause, "NumPad 7");
        controls.put(Control.stop, "NumPad 9");
        controls.put(Control.nextSong, "NumPad 6");
        controls.put(Control.previousSong, "NumPad 4");
        controls.put(Control.volumeUp, "NumPad Add");
        controls.put(Control.volumeDown, "NumPad Subtract");
    }
    
    // MARK: - Statics
    
    static Window mainWindow;
    static Coordinator applicationCoordinator;

    // MARK: - Main
    
    public static void main(String[] args) throws IOException {
        ConfigurationResult result = configureGlobalEnvironment();
        
        ColorScheme colorScheme = new DarkMode();
        mainWindow = new MainWindow(colorScheme);
        applicationCoordinator = new ApplicationCoordinator(colorScheme, result.hotkeyListener);
        applicationCoordinator.start(mainWindow);
        
        mainWindow.setVisible(true);
    }
    
    private static ConfigurationResult configureGlobalEnvironment() {
        BrowserPreferences.setChromiumSwitches("--disable-web-security", "--allow-file-access-from-files", "--allow-file-access", "--autoplay-policy=no-user-gesture-required");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib");
        System.setProperty("sun.java2d.noddraw", "true");
        
        HotkeyListener hotkeyListener = null;
        try{
            Logger l = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            l.setLevel(Level.OFF);
            l.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
            hotkeyListener = new HotkeyListener(controls);
            GlobalScreen.addNativeKeyListener(hotkeyListener);
        }catch(Exception e){}
        
        return new ConfigurationResult(hotkeyListener);
    }
    
    private static class ConfigurationResult {
        final HotkeyListener hotkeyListener;
        
        public ConfigurationResult(HotkeyListener hotkeyListener) {
            this.hotkeyListener = hotkeyListener;
        }
    }
    
}
