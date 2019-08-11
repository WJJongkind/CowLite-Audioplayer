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
import cap.gui.overlay.SongInfoOverlay;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;

/**
 * (c) Copyright This class initializes CowLite Audio Player. Main class, yippy!
 *
 * @author Wessel Jongkind
 */
public class CowLiteAudioPlayer {

    // MARK: - Constants
    
    private static final double minimumWindowOverlapFactor = 0.1;
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
    
    static Coordinator applicationCoordinator;

    // MARK: - Main
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Configure the application-wide environment
        configureGlobalEnvironment();

        // UI layout
        ColorScheme colorScheme = new DarkMode();

        // Overlay. We alreayd need this because the overlay has an inherent preferred size which we will give as input to the appstate service as a default value.
        SongInfoOverlay overlay = new SongInfoOverlay(colorScheme);

        // App state that persists through sessions
        int centerX = (int) Math.round(Toolkit.getDefaultToolkit().getScreenSize().width / 2.0 - overlay.getWidth() / 2.0);
        AppStateService appStateService = new AppStateService(controls, 0.5, Playlist.PlaylistMode.normal, new Dimension(1280, 720), new Point(200, 200), overlay.getSize(), new Point(centerX, 0), false, false);

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
        applicationCoordinator = new ApplicationCoordinator(colorScheme, hotkeyListener, playlistPlayer, playlistStore, new DefaultMenuContext(playlistPlayer, playlistStore), appStateService, overlay);

        // Update overlay with stored settings
        overlay.setSize(appStateService.getOverlaySize());
        overlay.setLocation(appStateService.getOverlayLocation());
        overlay.volumeChanged(playlistPlayer.getPlayer(), playlistPlayer.getPlayer().getVolume());
        playlistPlayer.getPlayer().addObserver(overlay);

        // Instantiate UI
        DefaultWindow mainWindow = new DefaultWindow(colorScheme);
        mainWindow.setLocation(appStateService.getWindowLocation());
        mainWindow.setSize(appStateService.getWindowSize());
        mainWindow.setFullScreen(appStateService.isWindowFullScreen());
        applicationCoordinator.start(mainWindow);
        
        // Ensure that the main window is visible on one of the available monitors
        ensureVisibility(mainWindow);
        
        // Make UI visible
        mainWindow.setVisible(true);
        overlay.setVisible(appStateService.isOverlayEnabled());
    }

    private static void configureGlobalEnvironment() {
        // JxBrowser preferences
        BrowserPreferences.setChromiumSwitches("--disable-web-security", "--allow-file-access-from-files", "--allow-file-access", "--autoplay-policy=no-user-gesture-required");

        // VLC for audio decoding support
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib");

        // Decreases RAM & CPU usage on some systems
        System.setProperty("sun.java2d.noddraw", "true");

        // Hotkey support
        try {
            Logger l = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            l.setLevel(Level.OFF);
            l.setUseParentHandlers(false);
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ensureVisibility(JFrame window) {
        GraphicsEnvironment grapihcsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = grapihcsEnvironment.getScreenDevices();
        
        for (GraphicsDevice graphicsDevice : graphicsDevices) {
            for (GraphicsConfiguration configuration : graphicsDevice.getConfigurations()) {
                Rectangle windowBounds = window.getBounds();
                Rectangle monitorBounds = configuration.getBounds();
                double windowPixelCount = windowBounds.getWidth() * windowBounds.getHeight();
                double monitorPixelCount = monitorBounds.getWidth() * monitorBounds.getHeight();

                Rectangle intersection = windowBounds.intersection(monitorBounds);
                double overlapFactor = (intersection.getWidth() * intersection.getHeight()) / (windowPixelCount);
                if(overlapFactor > minimumWindowOverlapFactor && intersection.getWidth() > 0 && intersection.getHeight() > 0 && windowPixelCount <= monitorPixelCount) {
                    return;
                }
            }
        }
        
        moveWindowsToCenterOfDefaultScreen(window);
    }
    
    private static void moveWindowsToCenterOfDefaultScreen(JFrame window) {
        // TODO verify this on MAC systems. For Windows it works (.getScreenSize() returns the size of the primary monitor on which we can center.
        Dimension primaryMonitorSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int newWidth = primaryMonitorSize.width / 2;
        int newHeight= primaryMonitorSize.height / 2;
        int centerX = primaryMonitorSize.width / 2 - newWidth / 2;
        int centerY = primaryMonitorSize.height / 2 - newHeight / 2;
        
        window.setBounds(centerX, centerY, newWidth, newHeight);
    }
    
}
