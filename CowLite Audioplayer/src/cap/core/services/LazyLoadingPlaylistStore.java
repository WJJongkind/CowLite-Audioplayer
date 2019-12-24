/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.services;

import cap.audio.Playlist;
import static cap.util.SugarySyntax.doTry;
import static cap.util.SugarySyntax.unwrappedPerform;
import filedatareader.FileDataReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that implements the PlaylistStoreInterface and lazely loads all playlists
 * on a background thread. This is useful when not all playlists are needed
 * immediately after initialising this object.
 * @author Wessel Jongkind
 */
public class LazyLoadingPlaylistStore implements PlaylistStoreInterface {
    
    // MARK: - Private properties
    
    private final List<PlaylistReference> playlistReferences = new ArrayList<>();
    private final List<WeakReference<PlaylistStoreObserver>> observers = new ArrayList<>();
    private final File storeFile;
    private final LazyLoadingPlaylistService playlistService;
    
    private BackgroundPlaylistLoadingThread backgroundLoadingThread;
    
    // MARK: - Initialisers
    
    /**
     * Initialises the LazyLoadingPlaylistStore.
     * @param file The file in which all files of stored playlists are defined.
     * @throws IOException
     * @throws URISyntaxException 
     */
    public LazyLoadingPlaylistStore(File file) throws IOException, URISyntaxException {
        this.storeFile = file;
        this.playlistService = new LazyLoadingPlaylistService();
        
        doTry(() -> {
            FileDataReader reader = new FileDataReader();
            reader.setPath(file);

            List<String> storedPlaylistPaths = reader.getDataStringLines();
            ArrayList<LazyLoadablePlaylist> playlists = new ArrayList<>();

            for(String playlistFilePath : storedPlaylistPaths) {
                File playlistFile = new File(playlistFilePath);
                LazyLoadablePlaylist playlist = new LazyLoadablePlaylist(playlistFile.getName(), playlistFile, playlistService);
                playlistReferences.add(new PlaylistReference(playlist, playlistFile));
                playlists.add(playlist);
            }

            this.backgroundLoadingThread = new BackgroundPlaylistLoadingThread(playlistService, playlists);
            this.backgroundLoadingThread.start(); // TODO maybe make this configurable?
        });
    }
    
    // MARK: - PlaylistStoreInterface

    @Override
    public void addPlaylist(Playlist playlist, File file) throws IOException {
        removePlaylistReference(playlist);
        playlistService.savePlayList(playlist, file);
        playlistReferences.add(new PlaylistReference(playlist, file));
        updateSaveFile();
        unwrappedPerform(observers, observer -> observer.didAddPlaylist(this, playlist));
    }

    @Override
    public List<Playlist> getPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        for(PlaylistReference reference : this.playlistReferences) {
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
        Iterator<PlaylistReference> playlistIterator = playlistReferences.iterator();
        
        while(playlistIterator.hasNext()) {
            PlaylistReference reference = playlistIterator.next();
            if(reference.playlist.getName().equals(playlist.getName())) {
                playlistIterator.remove();
                break;
            }
        }
    }
    
    private void updateSaveFile() throws IOException{
        // TODO this function really should be optimized. It won't cause much of an issue as is,
        // but this is still a dirty implementation.
        PrintWriter out = null;
        try {
            storeFile.delete();
            storeFile.getParentFile().mkdirs();
            storeFile.createNewFile();
            out = new PrintWriter(storeFile);
            for(PlaylistReference reference: playlistReferences) {
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
        public Playlist playlist;
        public File file;
        
        public PlaylistReference(Playlist playlist, File file) {
            this.playlist = playlist;
            this.file = file;
        }
    }
    
}
