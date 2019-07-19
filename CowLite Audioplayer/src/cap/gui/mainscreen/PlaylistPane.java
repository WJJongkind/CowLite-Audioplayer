/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.core.audio.Song;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.PlaylistPaneColorScheme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wessel
 */
public class PlaylistPane<SongType extends Song> extends JScrollPane {
    
    // MARK: - Associated types & constants
    
    public interface SongSelectionDelegate<SongType extends Song> {
        public void didSelectSong(SongType song);
    }
    
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        
        // MARK: - Private properties
        
        private final PlaylistPaneColorScheme colorScheme;
        
        // MARK: - Initialisers
        
        public AlternatingRowRenderer(PlaylistPaneColorScheme colorScheme) {
            this.colorScheme = colorScheme;
        }
        
        // MARK: - DefaultTableCellRenderer
        
        @Override
        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value, 
                                                       boolean isSelected, 
                                                       boolean hasFocus,
                                                       int row, 
                                                       int column) {
            Component c = super.getTableCellRendererComponent(table, 
                value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            if(isSelected) {
                c.setBackground(colorScheme.highlightBackgroundColor());
                c.setForeground(colorScheme.highlightTextColor());
            } else {
                c.setForeground(colorScheme.textColor());
                c.setBackground(row%2==0 ? colorScheme.firstBackgroundColor() : colorScheme.secondBackgroundColor());
            }
            return c;
        };
    }
    
    // MARK: - UI elements
    
    private final JTable songTable;
    private final DefaultTableModel songTableModel;
    
    // MARK: - Private elements
    
    private List<SongType> songs;
    private WeakReference<SongSelectionDelegate> delegate;
    private ListSelectionListener songSelectionListener;
    
    // MARK: - Initialisers
    
    public PlaylistPane(ColorScheme colorScheme) {
        super();
        songTable = new JTable();
        songTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        songTableModel.setColumnCount(3);
        
        songTable.setBackground(colorScheme.playlist().firstBackgroundColor());
        songTable.setForeground(colorScheme.playlist().textColor());
        songTable.setTableHeader(null);
        songTable.setModel(songTableModel);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.getSelectionModel().addListSelectionListener((songSelectionListener = e -> didSelectSong(e)));
        songTable.setShowGrid(false);
        songTable.setFont(songTable.getFont().deriveFont(Font.BOLD));
        songTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer(colorScheme.playlist()));
        
        
        super.setViewport(super.createViewport());
        super.getViewport().add(songTable);
        super.getViewport().setBackground(colorScheme.playlist().firstBackgroundColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder( 0, 0, 0, 0 ));
        super.setPreferredSize(super.getMinimumSize());
    }
    
    // MARK: - Getters & Setters
    
    public void setSongs(List<SongType> songs) {
        songTable.getSelectionModel().removeListSelectionListener(songSelectionListener);
        
        this.songs = songs;
        songTableModel.setRowCount(0);
        
        for(Song song : songs) {
            String[] newRow = {song.getSongName(), song.getArtistName(), song.getAlbumName()};
            songTableModel.addRow(newRow);
        }
        
        if(songs.size() == 0) {
            return;
        }
        
        songTable.setRowSelectionInterval(0, 0);
        songTable.getSelectionModel().addListSelectionListener(songSelectionListener);
    }
    
    public void setDelegate(SongSelectionDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public void setActiveSong(SongType song) {
        if(songs == null) {
            return;
        }
        
        songTable.getSelectionModel().removeListSelectionListener(songSelectionListener);
        int index = songs.indexOf(song);
        
        if(index != -1) {
            songTable.setRowSelectionInterval(index, index);
        } else {
            songTable.clearSelection();
        }
        
        songTable.getSelectionModel().addListSelectionListener(songSelectionListener);
    }
    
    // MARK: - ListSelectionListener
    
    private void didSelectSong(ListSelectionEvent event) {
        if(event.getValueIsAdjusting()) {
            return;
        }
        
        SongSelectionDelegate strongDelegate = delegate.get();
        if(delegate != null) {
            strongDelegate.didSelectSong(songs.get(songTable.getSelectedRow()));
        }
    }
    
}