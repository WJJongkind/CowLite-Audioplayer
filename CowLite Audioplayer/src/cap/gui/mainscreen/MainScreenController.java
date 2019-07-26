/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.core.audio.PlaylistPlayer;
import cap.core.audio.Playlist;
import cap.core.audio.Playlist.PlaylistMode;
import cap.core.audio.Song;
import cap.core.audio.SongPlayer;
import cap.core.audio.SongPlayer.SongPlayerObserver;
import cap.core.audio.files.FileSong;
import cap.core.audio.youtube.YouTubeService;
import cap.core.audio.youtube.YouTubeSong;
import cap.gui.ViewController;
import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.nilCoalesce;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author Wessel
 */
public class MainScreenController implements SongPlayerObserver<Song>, MainScreen.MainScreenDelegate, ViewController, DropTargetListener {
    
    // MARK: - Constants & associated types
    
    private static class Consants {
        public static final int updateInterval = 50;
        public static final Set<String> supportedFileTypes = new HashSet<>();
        
        static {
            String[] supportedExtensions = {"3pg", "aac", "act", "aiff", "flac", "gsm", "m4a", "m4p", "mp3", "ogg", "oga", "mogg", "opus", "vox", "webm", "wma", "mp4", "avi", "wmv", "wav", "flv", "mov"};
            supportedFileTypes.addAll(Arrays.asList(supportedExtensions));
        }
    }
    
    // MARK: - Private properties
    
    private final MainScreen mainScreen;
    private final PlaylistPlayer playlistPlayer;
    private final Timer timer;
    private final YouTubeService youTubeService;
    private DropTarget dropTarget;
    
    // MARK: - Initialisers
    
    public MainScreenController(ColorScheme colorScheme, PlaylistPlayer playlistPlayer, YouTubeService youTubeService) {
        // YT Service
        this.youTubeService = youTubeService;
        
        // Setting up the main screen
        this.mainScreen = new MainScreen(colorScheme);
        this.mainScreen.getVolumeSlider().setValue(playlistPlayer.getPlayer().getVolume());
        this.mainScreen.setDelegate(this);
        
        // Input handling
        dropTarget = new DropTarget(mainScreen, this);
        mapKeystrokes();
        
        // Playback
        this.playlistPlayer = playlistPlayer;
        this.playlistPlayer.getPlayer().addObserver(this);
        this.timer = new Timer(Consants.updateInterval, e -> updateTimeTrack());
        timer.start();
    }
    
    // MARK: - PlaylistPlayerDelegate

    @Override
    public void stateChanged(SongPlayer<Song> player, SongPlayer.PlayerState state) {
        switch(state) {
            case playing:
                mainScreen.getControlPane().enablePauseButton();
            default:
                mainScreen.getControlPane().enablePlayButton();
        }
    }

    @Override
    public void volumeChanged(SongPlayer<Song> player, double volume) {
        mainScreen.getVolumeSlider().setValue(volume);
        mainScreen.getVolumeSlider().repaint();
    }

    @Override
    public void songChanged(SongPlayer<Song> player, Song song) {
        mainScreen.getPlaylistPane().setActiveSong(song);
    }

    @Override
    public void didSeek(SongPlayer<Song> player, long position) {
        double percentageDone = position / ((double) player.getDuration());
        mainScreen.getTrackPositionSlider().setValue(percentageDone);
    }
    
    // MARK: - MainScreenDelegate

    @Override
    public void didChangeVolume(double newValue) {
        playlistPlayer.getPlayer().setVolume(newValue);
    }

    @Override
    public void didChangeTrackPosition(double position) {
        playlistPlayer.getPlayer().seek((long) Math.round(position * playlistPlayer.getPlayer().getDuration()));
    }

    @Override
    public boolean didPressPlayButton(MusicControlPane sender) {
        if(playlistPlayer.getPlayer().getSong() == null) {
            return playlistPlayer.playNextSong();
        } else {
            return playlistPlayer.getPlayer().play();
        }
    }

    @Override
    public void didPressPauseButton(MusicControlPane sender) {
        playlistPlayer.getPlayer().pause(); // TODO make sure that play button is set.
   }

    @Override
    public void didPressPreviousButton(MusicControlPane sender) {
        playlistPlayer.playPreviousSong();
    }

    @Override
    public void didPressNextButton(MusicControlPane sender) {
        playlistPlayer.playNextSong();
    }

    @Override
    public void didPressStopButton(MusicControlPane sender) {
        playlistPlayer.getPlayer().stop();
    }

    @Override
    public boolean didPressShuffleButton(MusicControlPane sender) {
        PlaylistMode currentMode = playlistPlayer.getPlaylist().getMode();
        playlistPlayer.getPlaylist().setMode(currentMode == PlaylistMode.shuffled ? PlaylistMode.normal : PlaylistMode.shuffled);
        mainScreen.getPlaylistPane().setSongs(playlistPlayer.getPlaylist().getSongs());
        playlistPlayer.getPlayer().setSong(playlistPlayer.getPlaylist().getSong(0));
        return playlistPlayer.getPlaylist().getMode() == PlaylistMode.shuffled;
    }

    @Override
    public boolean didPressAlphabeticSortButton(MusicControlPane sender) {
        PlaylistMode currentMode = playlistPlayer.getPlaylist().getMode();
        playlistPlayer.getPlaylist().setMode(currentMode == PlaylistMode.alphabetic ? PlaylistMode.normal : PlaylistMode.alphabetic);
        mainScreen.getPlaylistPane().setSongs(playlistPlayer.getPlaylist().getSongs());
        playlistPlayer.getPlayer().setSong(playlistPlayer.getPlaylist().getSong(0));
        return playlistPlayer.getPlaylist().getMode() == PlaylistMode.alphabetic;
    }

    @Override
    public void didPressClearButton(MusicControlPane sender) {
        playlistPlayer.getPlayer().stop();
        playlistPlayer.setPlaylist(new Playlist());
        mainScreen.getPlaylistPane().setSongs(playlistPlayer.getPlaylist().getSongs());
    }

    @Override
    public void didSelectPlayList(Playlist playlist) {
        playlistPlayer.setPlaylist(playlist);
    }

    @Override
    public void didSelectSong(Song song) {
        playlistPlayer.playSongIfPresentInPlaylist(song);
    }
    
    // MARK: - ViewController
    
    @Override
    public JPanel getView() {
        return mainScreen;
    }
    
    // MARK: - DropTargetListener
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {}

    @Override
    public void dragOver(DropTargetDragEvent dtde) {}

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {}

    @Override
    public void dragExit(DropTargetEvent dte) {}

    @Override
    public void drop(DropTargetDropEvent dtde) {
        // Accept copy drops
        dtde.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = dtde.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for(DataFlavor flavor : flavors) {
            try {
                // If the drop items are files
                if(flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
                    List<File> files = (List<File>) transferable.getTransferData(flavor);
                    List<File> collected = new ArrayList<>();

                    // Loop them through
                    for(Object file: files) {
                        File theFile = (File) file;
                        collectFiles(theFile, collected);
                    }
                    
                    // Inform that the drop is complete
                    dtde.dropComplete(true);
                    
                    // Add new songs to the playlist
                    for(File f : collected) {
                        String fileType = getFileExtension(f);
                        if(fileType != null && Consants.supportedFileTypes.contains(fileType.toLowerCase())) {
                            playlistPlayer.getPlaylist().addSong(new FileSong(f));
                        }
                    }
                    
                    // Notify the playlist player that new songs were added
                    playlistPlayer.refresh();
                    
                    // Make sure UI is still in sync
                    List<Song> songs = playlistPlayer.getPlaylist().getSongs();
                    mainScreen.getPlaylistPane().setSongs(songs);
                    mainScreen.getPlaylistPane().setActiveSong(nilCoalesce(playlistPlayer.getPlayer().getSong(), songs.get(0)));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
    
    private String getFileExtension(File f) {
        String fileName = f.getName();
        int separatorIndex;
        if((separatorIndex = fileName.lastIndexOf(".")) != -1 && fileName.length() - separatorIndex > 0) {
            return fileName.substring(separatorIndex + 1);
        } else {
            return null;
        }
    }
    
    private void collectFiles(File f, List<File> collected) {
        if(f.isFile())
            collected.add(f);
        else if(f.isDirectory())
        {
            File[] files = f.listFiles();
            for(File file : files)
                collectFiles(file, collected);
        }
    }
    
    // MARK: - Timer ActionListener
    
    private void updateTimeTrack() {
        // Sometimes, obtaining duration & position fails. To keep the Timer from firing events, catch any exception that may occur.
        try {
            double percentageDone = playlistPlayer.getPlayer().getPosition() / ((double) playlistPlayer.getPlayer().getDuration());
            mainScreen.getTrackPositionSlider().setValue(percentageDone);
            mainScreen.getTrackPositionSlider().repaint();
        } catch(Exception e) {}
    }
    
    // MARK: - Keybindings for pasting YouTube videos
    
    private void mapKeystrokes() {
        mainScreen.getInputMap().put(KeyStroke.getKeyStroke("ctrl V"), "tryPasteUrl");
        mainScreen.getActionMap().put("tryPasteUrl", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object pastedData = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    
                    if(pastedData != null) {
                        String text = (String) pastedData;
                        
                        List<YouTubeSong> foundSongs = youTubeService.readUrl(text);
                        for(YouTubeSong foundSong : foundSongs) {
                            playlistPlayer.getPlaylist().addSong(foundSong);
                        }
                        
                        playlistPlayer.refresh();
                        mainScreen.getPlaylistPane().setSongs(playlistPlayer.getPlaylist().getSongs());
                        mainScreen.getPlaylistPane().repaint();
                    }
                } catch (Exception ex) {
                    // TODO show some user feedback?
                }
            }
        });
    }
    
}
