/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.files;

import cap.util.ResultCarryingCountdownLatch;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventListener;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.Picture;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.State;

/**
 * Class with which all metadata of songs on the file-system can be read, if present.
 * @author Wessel Jongkind
 */
class FileSongMetaDataReader {
    
    // MARK: - Public associated types & constants
    
    public final class MetaData {
        public final String artist;
        public final String album;
        public final String song;
        public final long duration;
        
        public MetaData(String artist, String album, String song, long duration) {
            this.artist = artist;
            this.album = album;
            this.song = song;
            this.duration = duration;
        }
    }
    
    // MARK: - Constants
    
    private static final int timeout = 1500;
    
    // MARK: - Private properties
    
    private final MediaPlayer mediaPlayer = new MediaPlayerFactory().mediaPlayers().newMediaPlayer();
    
    private ResultCarryingCountdownLatch<MetaData> countdownLatch;
    
    // MARK: - Public methods
    
    /**
     * Tries to obtain the metadata of the song file located at the given URL. If
     * the given URL contains a file that has no song metadata, then the behavior of
     * this function is undefined.
     * @param url The URL where the file is located at on the local filesystem.
     * @return The MetaData associated to the song. The artist/album/song fields may be null if parsing partially failed. Duration is always returned.
     */
    public synchronized MetaData readMetaData(URL url) {
        mediaPlayer.media().prepare(url.getProtocol() + "://" + url.getPath());
        try {
            countdownLatch = new ResultCarryingCountdownLatch<>(1);
            MediaEventListener listener = new MediaEventListenerImpl();
            mediaPlayer.media().events().addMediaEventListener(listener);
            mediaPlayer.media().parsing().parse(timeout);
            MetaData result = countdownLatch.awaitResult();
            mediaPlayer.media().events().removeMediaEventListener(listener);
            return result;
        } catch (InterruptedException ex) {
            return new MetaData(null, null, null, mediaPlayer.media().info().duration());
        }
            
    }
    
    // MARK: - Private associated types
    
    private final class MediaEventListenerImpl implements MediaEventListener {

        @Override
        public void mediaMetaChanged(Media media, Meta meta) {
        }

        @Override
        public void mediaSubItemAdded(Media media, MediaRef mr) {
        }

        @Override
        public void mediaDurationChanged(Media media, long l) {
        }

        @Override
        public void mediaParsedChanged(Media media, MediaParsedStatus mps) {
            if(mps == MediaParsedStatus.DONE) {
                String artist = media.meta().asMetaData().get(Meta.ARTIST);
                String album = media.meta().asMetaData().get(Meta.ALBUM);
                String title = media.meta().asMetaData().get(Meta.TITLE);
                long duration = media.info().duration();

                countdownLatch.countDown(new MetaData(artist, album, title, duration));
            } else {
                countdownLatch.countDown(null);
            }
        }

        @Override
        public void mediaFreed(Media media, MediaRef mr) {
        }

        @Override
        public void mediaStateChanged(Media media, State state) {
        }

        @Override
        public void mediaSubItemTreeAdded(Media media, MediaRef mr) {
        }

        @Override
        public void mediaThumbnailGenerated(Media media, Picture pctr) {
        }
    }
    
}
