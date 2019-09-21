/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.gui.shared.SexyScrollPane;
import cap.audio.Song;
import cap.gui.colorscheme.TableColorScheme;
import java.awt.Component;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.shared.Table;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.util.ArrayList;

/**
 *
 * @author Wessel
 */
public class PlaylistPane<SongType extends Song> extends SexyScrollPane implements Table.Delegate {
    
    // MARK: - Associated types & constants
    
    public interface SongSelectionDelegate<SongType extends Song> {
        public void didSelectSong(SongType song);
        public void songMoved(SongType song, int index);
    }
    
    // MARK: - UI elements
    
    private final Table songTable;
    
    // MARK: - Private elements
    
    private List<SongType> songs;
    private WeakReference<SongSelectionDelegate> delegate;
    
    // MARK: - Initialisers
    
    public PlaylistPane(ColorScheme colorScheme) {
        super(colorScheme.general().getFrameColor());
        songTable = new Table(colorScheme.playlist(), colorScheme.font().s().bold(), 3);
        songTable.setDelegate(this);
        
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
        this.songs = songs;
        
        songTable.setDelegate(null);
        songTable.clearRows();
        
        for(Song song : songs) {
            String[] newRow = {song.getSongName(), song.getArtistName(), song.getAlbumName()};
            songTable.addRow(newRow);
        }
        
        songTable.selectRow(0);
        songTable.setDelegate(this);
        
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
        songTable.addRow(newRow);
        
//        songTable.getSelectionModel().removeListSelectionListener(songSelectionListener);
//        songTable.setSelectedRow(selectedIndex, selectedIndex);
//        songTable.getSelectionModel().addListSelectionListener(songSelectionListener);
//        
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
        
        songTable.setDelegate(null);
        int index = songs.indexOf(song);
        songTable.selectRow(songs.indexOf(song));
        songTable.setDelegate(this);
        
        if(index < 0) {
            return;
        }
        
        // Ensuring that the active song becomes visible
        songTable.scrollRectToVisible(songTable.getCellRect(index, 0, true));
        
        super.revalidate();
        super.repaint();
    }
    
    // MARK: - TableDelegate
    
    @Override
    public void didSelectRow(Table sender, int row) {
        unwrappedPerform(delegate, delegate -> delegate.didSelectSong(songs.get(row)));
    }

    @Override
    public void didMoveRow(Table sender, int from, int to) {
        SongType song = songs.remove(from);
        songs.add(to, song);
        
        songTable.selectRow(to);
        
        super.revalidate();
        super.repaint();
        
        unwrappedPerform(delegate, delegate -> delegate.songMoved(song, to));
    }
    
    // MARK: - Private associated types
    
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        
        // MARK: - Private properties
        
        private final TableColorScheme colorScheme;
        
        // MARK: - Initialisers
        
        public AlternatingRowRenderer(TableColorScheme colorScheme) {
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
