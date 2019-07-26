/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.menu;

import cap.core.audio.Playlist;
import cap.core.services.PlaylistStoreInterface;

/**
 *
 * @author Wessel
 */
public interface MenuContextInterface {
    public PlaylistStoreInterface getPlaylistStore();
    public Playlist getCurrentPlaylist();
    public String getAboutText();
    public String getFeaturesText();
}
