/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.files;

import cap.util.ResultCarryingCountdownLatch;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventListener;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.Picture;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.State;

/**
 *
 * @author Wessel
 */
class FileSongMetaDataReader implements MediaEventListener {
    
    private static final int timeout = 1500;
    private final ResultCarryingCountdownLatch<MetaData> latch = new ResultCarryingCountdownLatch<>(1);
    
    public synchronized MetaData readMetaData(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.media().events().addMediaEventListener(this);
            mediaPlayer.media().parsing().parse(timeout);
            MetaData result = latch.awaitResult();
            mediaPlayer.media().events().removeMediaEventListener(this);
            
            return result;
        } catch (InterruptedException ex) {
            return null;
        }
    }

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
            
            latch.countDown(new MetaData(artist, album, title, duration));
        } else {
            latch.countDown(null);
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
    
    public static class MetaData {
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
    
}
