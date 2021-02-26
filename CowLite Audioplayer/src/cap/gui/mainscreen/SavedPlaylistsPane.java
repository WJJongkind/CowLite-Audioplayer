/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.audio.Playlist;
import cap.gui.colorscheme.SavedListsPaneColorScheme;
import java.awt.Component;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Point;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Wessel
 */
public class SavedPlaylistsPane extends JScrollPane {
    
    // MARK: - Associated types & constants
    
    public interface PlayListSelectionDelegate {
        public void didSelectPlaylist(Playlist playList);
    }
    
    // MARK: - UI properties
    
    private final JList playlistPane;
    private final DefaultListModel<String> playlistListModel;
    
    // MARK: - Private properties
    
    private final ListSelectionListener selectionListener = e -> didSelectPlaylist();
    
    private WeakReference<PlayListSelectionDelegate> delegate;
    private List<Playlist> playlists;
    
    
    // MARK: - Initialiser
    
    public SavedPlaylistsPane(ColorScheme colorScheme) {
        playlistPane = new JList() {
            @Override
            public int locationToIndex(Point location) {
                int index = super.locationToIndex(location);
                if (index != -1 && !getCellBounds(index, index).contains(location)) {
                    return -1;
                } else {
                    return index;
                }
            }
        };
        playlistListModel = new DefaultListModel<>();
        
        playlistPane.setBackground(colorScheme.savedLists().getBackgroundColor());
        playlistPane.setForeground(colorScheme.savedLists().getTextColor());
        playlistPane.addListSelectionListener(selectionListener);
        playlistPane.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        playlistPane.setModel(playlistListModel);
        playlistPane.setCellRenderer(new ListCellRenderer(colorScheme.savedLists()));
                
        super.setViewport(super.createViewport());
        super.getViewport().add(playlistPane);
        super.getViewport().setBackground(colorScheme.savedLists().getBackgroundColor());
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
        
        playlists.sort((a, b) -> {
            return a.getName().compareTo(b.getName());
        });
        
        for(Playlist playlist: playlists) {
            playlistListModel.addElement(playlist.getName());
        }
    }
    
    public void clearSelection() {
        // Removing and re-adding selection listener is required because otherwise the delegate
        // will be notified that index 0 has been selected,which is bad as that is not the case.
        playlistPane.removeListSelectionListener(selectionListener);
        playlistPane.clearSelection();
        playlistPane.addListSelectionListener(selectionListener);
    }
    
    // MARK: - ListSelectionListener
    
    private void didSelectPlaylist() {
        int index = playlistPane.getSelectedIndex();
        
        if(index == -1) {
            return;
        }
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectPlaylist(playlists.get(index)));
    }
    
    // MARK: - Private associated types
    
    private class ListCellRenderer extends DefaultListCellRenderer {
        
        // MARK: - Private properties
        
        private final SavedListsPaneColorScheme colorScheme;
        
        // MARK: - Initialisers
        
        public ListCellRenderer(SavedListsPaneColorScheme colorScheme) {
            this.colorScheme = colorScheme;
        }
        
        // MARK: - DefaultTableCellRenderer
        
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(noFocusBorder);
            if(isSelected) {
                c.setBackground(colorScheme.getHighlightBackgroundColor());
                c.setForeground(colorScheme.getHighlightTextColor());
            } else {
                c.setForeground(colorScheme.getTextColor());
                c.setBackground(getBackground());
            }
            return c;
        };
    }
    
}
