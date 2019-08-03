package cap.core;

import cap.control.HotkeyListener;
import cap.audio.DynamicSongPlayer;
import cap.audio.Playlist;
import cap.audio.PlaylistPlayer;
import cap.control.HotkeyListener.Control;
import cap.core.services.AppStateService;
import cap.core.services.PlaylistService;
import cap.core.services.PlaylistStore;
import cap.gui.DefaultWindow;
import cap.gui.Window;
import cap.gui.colorscheme.darkmode.DarkMode;
import com.sun.jna.NativeLibrary;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import java.io.File;
import java.net.URISyntaxException;
import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.nilCoalesce;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

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
        configureGlobalEnvironment();
        
        // UI layout
        ColorScheme colorScheme = new DarkMode();
        
        // App state that persists through sessions
        AppStateService appStateService = new AppStateService(controls, 0.5, Playlist.PlaylistMode.normal, new Dimension(1280,720), new Point(200, 200), false);
        
        // Music playback
        PlaylistPlayer playlistPlayer = new PlaylistPlayer(new DynamicSongPlayer());
        playlistPlayer.getPlayer().setVolume(appStateService.getVolume());
        playlistPlayer.getPlaylist().setMode(appStateService.getPlaylistMode());
        
        // Storing playlists
        PlaylistStore playlistStore = new PlaylistStore(new File("resources" + File.separatorChar + "persistence" + File.separatorChar + "playlists.store"), new PlaylistService());
        
        // Hotkey events for when the app is not focussed
        HotkeyListener hotkeyListener = new HotkeyListener(appStateService.getControls());
        GlobalScreen.addNativeKeyListener(hotkeyListener);
        
        // Main coordinator
        applicationCoordinator = new ApplicationCoordinator(colorScheme, hotkeyListener, playlistPlayer, playlistStore, new DefaultMenuContext(playlistPlayer, playlistStore), appStateService);
        
        // Initiate UI
        mainWindow = new DefaultWindow(colorScheme);
        mainWindow.setLocation(appStateService.getWindowLocation());
        mainWindow.setSize(appStateService.getWindowSize());
        mainWindow.setFullScreen(appStateService.isWindowFullScreen());
        applicationCoordinator.start(mainWindow);
        mainWindow.setVisible(true);
    }
    
    private static void configureGlobalEnvironment() {
        // JxBrowser preferences
        BrowserPreferences.setChromiumSwitches("--disable-web-security", "--allow-file-access-from-files", "--allow-file-access", "--autoplay-policy=no-user-gesture-required");
        
        // VLC for audio decoding support
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib");
        
        // Decreases RAM & CPU usage on some systems
        System.setProperty("sun.java2d.noddraw", "true");
        
        // Hotkey support
        try{
            Logger l = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            l.setLevel(Level.OFF);
            l.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
