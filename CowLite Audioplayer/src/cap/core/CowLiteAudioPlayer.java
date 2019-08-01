package cap.core;

import cap.control.HotkeyListener;
import cap.control.HotkeyListener.Control;
import cap.core.audio.DynamicSongPlayer;
import cap.core.audio.PlaylistPlayer;
import cap.core.services.PlaylistStore;
import cap.gui.DefaultWindow;
import cap.gui.Window;
import cap.gui.colorscheme.darkmode.DarkMode;
import com.sun.jna.NativeLibrary;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import java.io.File;
import java.net.URISyntaxException;
import cap.gui.colorscheme.UILayout;

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
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Configure the application-wide environment
        ConfigurationResult result = configureGlobalEnvironment();
        
        // UI layout
        UILayout colorScheme = new DarkMode();
        
        // Music playback
        PlaylistPlayer playlistPlayer = new PlaylistPlayer(new DynamicSongPlayer());
        
        // Storing playlists
        PlaylistStore playlistStore = new PlaylistStore(new File("resources" + File.separatorChar + "infofiles" + File.separatorChar + "playlisstore"));
        
        // Initiate UI
        mainWindow = new DefaultWindow(colorScheme, new MenuContext(playlistPlayer, playlistStore));
        applicationCoordinator = new ApplicationCoordinator(colorScheme, result.hotkeyListener, playlistPlayer, playlistStore);
        applicationCoordinator.start(mainWindow);
        
        mainWindow.setVisible(true);
    }
    
    private static ConfigurationResult configureGlobalEnvironment() {
        // JxBrowser preferences
        BrowserPreferences.setChromiumSwitches("--disable-web-security", "--allow-file-access-from-files", "--allow-file-access", "--autoplay-policy=no-user-gesture-required");
        
        // VLC for audio decoding support
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib");
        
        // Decreases RAM & CPU usage on some systems
        System.setProperty("sun.java2d.noddraw", "true");
        
        // Hotkey support
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
