/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.core.audio.Playlist;
import cap.core.audio.PlaylistPlayer;
import cap.core.services.PlaylistStoreInterface;

/**
 *
 * @author Wessel
 */
class DefaultMenuContext implements DefaultMenuCoordinator.DefaultMenuContextInterface {
        
        private final PlaylistPlayer playlistPlayer;
        private final PlaylistStoreInterface playlistStore;

        public DefaultMenuContext(PlaylistPlayer playlistPlayer, PlaylistStoreInterface playlistStore) {
            this.playlistPlayer = playlistPlayer;
            this.playlistStore = playlistStore;
        }

        @Override
        public PlaylistStoreInterface getPlaylistStore() {
            return playlistStore;
        }

        @Override
        public Playlist getCurrentPlaylist() {
            return playlistPlayer.getPlaylist();
        }

        @Override
        public String getAboutText() {
            return "";
        }

        @Override
        public String getFeaturesText() {
            return "";
        }
        
    }