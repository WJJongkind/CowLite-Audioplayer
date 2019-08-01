/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.core.audio.Playlist;
import static cap.util.SugarySyntax.unwrappedPerform;
import filedatareader.FileDataReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Wessel
 */
public class PlaylistStore implements PlaylistStoreInterface {
    
    // MARK: - Private properties
    
    private final List<PlaylistReference> playlists = new ArrayList<>();
    private final List<WeakReference<PlaylistStoreObserver>> observers = new ArrayList<>();
    private final File storeFile;
    private final PlaylistServiceInterface playlistService;
    
    // MARK: - Initialisers
    
    public PlaylistStore(File file, PlaylistServiceInterface playlistService) throws IOException, URISyntaxException {
        this.storeFile = file;
        this.playlistService = playlistService;
        
        FileDataReader reader = new FileDataReader();
        reader.setPath(file);
        
        for(String playlistFilePath : reader.getDataStringLines()) {
            File playlistFile = new File(playlistFilePath);
            Playlist playlist = playlistService.loadPlayList(playlistFile);
            playlists.add(new PlaylistReference(playlist, playlistFile));
        }
    }
    
    // MARK: - PlaylistStoreInterface

    @Override
    public void addPlaylist(Playlist playlist, File file) throws IOException {
        removePlaylistReference(playlist);
        playlistService.savePlayList(playlist, file);
        playlists.add(new PlaylistReference(playlist, file));
        updateSaveFile();
        unwrappedPerform(observers, observer -> observer.didAddPlaylist(this, playlist));
    }

    @Override
    public List<Playlist> getPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        for(PlaylistReference reference : this.playlists) {
            playlists.add(reference.playlist);
        }
        return playlists;
    }

    @Override
    public void removePlaylist(Playlist playlist) throws IOException {
        removePlaylistReference(playlist);
        updateSaveFile();
        unwrappedPerform(observers, observer -> observer.didRemovePlaylist(this, playlist));
    }

    @Override
    public void addObserver(PlaylistStoreObserver observer) {
        observers.add(new WeakReference<>(observer));
    }
    
    // MARK: - Private methods
    
    private void removePlaylistReference(Playlist playlist) {
        Iterator<PlaylistReference> playlistIterator = playlists.iterator();
        
        while(playlistIterator.hasNext()) {
            PlaylistReference reference = playlistIterator.next();
            if(reference.playlist.getName().equals(playlist.getName())) {
                playlistIterator.remove();
                break;
            }
        }
    }
    
    private void updateSaveFile() throws IOException{
        PrintWriter out = null;
        try {
            storeFile.delete();
            storeFile.getParentFile().mkdirs();
            storeFile.createNewFile();
            out = new PrintWriter(storeFile);
            for(PlaylistReference reference: playlists) {
                out.println(reference.file);
            }
            out.flush();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            // Should never happen, as an IOException would already have been thrown upon createNewFile.
        } finally {
            out.close();
        }
    }
    
    // MARK: - Private associated types
    
    private class PlaylistReference {
        Playlist playlist;
        File file;
        
        public PlaylistReference(Playlist playlist, File file) {
            this.playlist = playlist;
            this.file = file;
        }
    }
    
}
