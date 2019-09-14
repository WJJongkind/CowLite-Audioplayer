/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.gui.shared.SexyScrollPane;
import cap.audio.Song;
import cap.gui.colorscheme.PlaylistPaneColorScheme;
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
import cap.gui.colorscheme.ColorScheme;
import java.util.ArrayList;

/**
 *
 * @author Wessel
 */
public class PlaylistPane<SongType extends Song> extends SexyScrollPane {
    
    // MARK: - Associated types & constants
    
    public interface SongSelectionDelegate<SongType extends Song> {
        public void didSelectSong(SongType song);
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
        super(colorScheme.general().getFrameColor());
        songTable = new JTable();
        songTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        songTableModel.setColumnCount(3);
        
        songTable.setBackground(colorScheme.playlist().getFirstBackgroundColor());
        songTable.setForeground(colorScheme.playlist().getTextColor());
        songTable.setTableHeader(null);
        songTable.setModel(songTableModel);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.getSelectionModel().addListSelectionListener((songSelectionListener = e -> didSelectSong(e)));
        songTable.setShowGrid(false);
        songTable.setFont(songTable.getFont().deriveFont(Font.BOLD));
        songTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer(colorScheme.playlist()));
        
        
        super.setViewport(super.createViewport());
        super.getViewport().add(songTable);
        super.getViewport().setBackground(colorScheme.playlist().getFirstBackgroundColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder());
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
        super.revalidate();
        super.repaint();
    }
    
    public void addSong(SongType song) {
        if(songs == null) {
            songs = new ArrayList<>();
        }
        songs.add(song);
        
        int selectedIndex = Math.max(0, songTable.getSelectedRow());
        String[] newRow = {song.getSongName(), song.getArtistName(), song.getAlbumName()};
        songTableModel.addRow(newRow);
        
        songTable.getSelectionModel().removeListSelectionListener(songSelectionListener);
        songTable.setRowSelectionInterval(selectedIndex, selectedIndex);
        songTable.getSelectionModel().addListSelectionListener(songSelectionListener);
        
        if(songs.size() == 0) {
            return;
        }
        
        super.revalidate();
        super.repaint();
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
        super.revalidate();
        super.repaint();
    }
    
    // MARK: - ListSelectionListener
    
    private void didSelectSong(ListSelectionEvent event) {
        super.revalidate();
        super.repaint();
        
        if(event.getValueIsAdjusting()) {
            return;
        }
        
        SongSelectionDelegate strongDelegate = delegate.get();
        if(delegate != null && songTable.getSelectedRow() > -1) {
            strongDelegate.didSelectSong(songs.get(songTable.getSelectedRow()));
        }
    }
    
    // MARK: - Private associated types
    
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
                c.setBackground(colorScheme.getHighlightBackgroundColor());
                c.setForeground(colorScheme.getHighlightTextColor());
            } else {
                c.setForeground(colorScheme.getTextColor());
                c.setBackground(row%2==0 ? colorScheme.getFirstBackgroundColor() : colorScheme.getSecondBackgroundColor());
            }
            return c;
        };
    }
    
}
