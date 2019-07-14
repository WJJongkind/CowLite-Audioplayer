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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
        songTableModel = new DefaultTableModel();
        songTableModel.setColumnCount(3);
        
        songTable.setBackground(colorScheme.backgroundColor());
        songTable.setForeground(colorScheme.textColor());
        songTable.setTableHeader(null);
        songTable.setModel(songTableModel);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.getSelectionModel().addListSelectionListener((songSelectionListener = e -> didSelectSong(e)));
        
        super.setViewport(super.createViewport());
        super.getViewport().add(songTable);
        super.getViewport().setBackground(colorScheme.backgroundColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder( 0, 0, 0, 0 ));
    }
    
    // MARK: - Getters & Setters
    
    public void setSongs(List<SongType> songs) {
        songTable.getSelectionModel().removeListSelectionListener(songSelectionListener);
        
        System.out.println("Setting songs to: " + songs);
        this.songs = songs;
        songTableModel.setRowCount(0);
        
        for(Song song : songs) {
            String[] newRow = {song.getSongName(), song.getArtistName(), song.getAlbumName()};
            songTableModel.addRow(newRow);
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
        songTable.setRowSelectionInterval(index, index);
        
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
