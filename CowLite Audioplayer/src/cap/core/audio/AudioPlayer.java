/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio;

import java.util.List;

/**
 *
 * @author MemeMeister
 */
public interface AudioPlayer
{
    public void play();

    public List<?> getList();
    
    public String getSongInfo();
    
    public void changeSong(int value);
    
    public void selectSong(int value);
    
    public void removeSong(int value);
    
    public void clearList();
    
    public void stop();
    
    public void setVolume(int value);
    
    public void changeVolume(int value);
    
    public double getVolume();
    
    public void shuffle();
    
    public void alphabetical();
    
    public void setPaused(boolean stat);
    
    public void loadList(String src);
    
    public void seek(int position);
    
    public boolean getShuffled();
    
    public boolean getAlphabetical();
    
    public int getAlphabeticalType();
    
    public int getIndex();
    
    public int getDuration();
    
    public int getPosition();
    
    public boolean isPlaying();
    
    public boolean isPaused();

    public boolean isFinished();
}
