/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.core.audio.Playlist;
import cap.core.audio.PlaylistPlayer;
import cap.gui.menu.MenuContextInterface;
import cap.core.services.PlaylistStoreInterface;

/**
 *
 * @author Wessel
 */
class MenuContext implements MenuContextInterface {
        
        private final PlaylistPlayer playlistPlayer;
        private final PlaylistStoreInterface playlistStore;

        public MenuContext(PlaylistPlayer playlistPlayer, PlaylistStoreInterface playlistStore) {
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