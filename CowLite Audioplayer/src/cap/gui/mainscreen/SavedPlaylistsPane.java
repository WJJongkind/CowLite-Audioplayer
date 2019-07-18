/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.core.audio.Playlist;
import cap.core.audio.Song;
import cap.gui.colorscheme.ColorScheme;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author Wessel
 */
public class SavedPlaylistsPane extends JScrollPane {
    
    // MARK: - Associated types & constants
    
    public interface PlayListSelectionDelegate {
        public void didSelectPlayList(Playlist playList);
    }
    
    // MARK: - UI properties
    
    private final JList playlistPane;
    private final DefaultListModel<String> playlistListModel;
    
    // MARK: - Private properties
    
    private WeakReference<PlayListSelectionDelegate> delegate;
    private List<Playlist> playlists;
    
    
    // MARK: - Initialiser
    
    public SavedPlaylistsPane(ColorScheme colorScheme) {
        playlistPane = new JList();
        playlistListModel = new DefaultListModel<>();
        
        playlistPane.setBackground(colorScheme.savedLists().backgroundColor());
        playlistPane.setForeground(colorScheme.savedLists().textColor());
        playlistPane.addListSelectionListener(e -> didSelectPlaylist(e.getFirstIndex()));
        playlistPane.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        playlistPane.setModel(playlistListModel);
                
        super.setViewport(super.createViewport());
        super.getViewport().add(playlistPane);
        super.getViewport().setBackground(colorScheme.savedLists().backgroundColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder( 0, 0, 0, 0 ));
    }
    
    // MARK: - Getters & setters
    
    public void setDelegate(PlayListSelectionDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public void setPlayLists(List<Playlist> playlists) {
        this.playlists = playlists;
        playlistListModel.clear();
        
        for(Playlist playlist: playlists) {
            playlistListModel.addElement(playlist.getName());
        }
    }
    
    // MARK: - ListSelectionListener
    
    private void didSelectPlaylist(int index) {
        if(delegate == null) {
            return;
        }
        
        PlayListSelectionDelegate strongDelegate = delegate.get();
        if(strongDelegate != null) {
            strongDelegate.didSelectPlayList(playlists.get(index));
        }
    }
    
}
