package cap.core.audio;

import cap.util.IO;
import java.io.BufferedReader;
import javafx.scene.media.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import javafx.util.Duration;

/**
 * (c) Copyright
 * This class handles all the audioplayer actions.
 * @author Wessel Jongkind
 */
public class FileAudioPlayer implements AudioPlayer
{
    //Playlists and volume/playlist index data.
    private int listIndex;
    private double volume = 75;
    private ArrayList<MetaData> shufflelist;
    private ArrayList<MetaData> list;
    private ArrayList<MetaData> alphabeticList;
    private int sortStyle;
    
    //Booleans that define the state of the player. Would be nice if we
    //could reduce the amount of booleans.
    private boolean shuffled;
    private boolean alphabetical;
    private boolean playing;
    private boolean paused;
    
    //The mediaplayers
    private MediaPlayer mediaplayer;
    private final int SORTSTYLE_LIMIT = 3, BY_SONG = 0, BY_ARTIST = 1, BY_ALBUM = 2;
    
    //Settings
    private final Map<String, String> SETTINGS;
    
    /**
     * retreives the settings for audioplayer and instantiates itself
     */
    public FileAudioPlayer(Map<String, String> audioSettings)
    {
        this.SETTINGS = audioSettings;
        
        list = new ArrayList<>();
        playing = false;
        paused = false;
        
        //Sets the settings relevant to this class
        setVolume(Integer.parseInt(SETTINGS.get("volume")));
            
        listIndex = -1;
    }

    //Starts playing a song/video at selected index
    @Override
    public void play()
    {
        try
        {
            if(getList() == null)
                return;
            
            //Creates the media that should be played
            Media media = null;
            if(!shuffled && !alphabetical)
                media = new Media(list.get(listIndex).getPath());
            if(alphabetical)
                media = new Media(alphabeticList.get(listIndex).getPath());
            if(shuffled)
                media = new Media(shufflelist.get(listIndex).getPath());

            //Creates a new mediaplayer to play the media
            mediaplayer = new MediaPlayer(media);
            mediaplayer.setVolume(volume);
            
            //Yay, we are playing
            mediaplayer.play();
            playing = true;
            paused = false;
            media = null;
      }catch(Exception e){e.printStackTrace();}
    }

    /**
     * Get the playlist that is being played
     * @return the active playlist
     */
    public ArrayList<MetaData> getList()
    {
        //returns the playlist that is being played.
        if(!shuffled && !alphabetical)
            return list;
        if(shuffled)
            return shufflelist;
        else
            return alphabeticList;
    }
    
    public String getSongInfo()
    {
        try{
            MetaData song = getList().get(listIndex);
            
            if(song.getDataFound())
                return song.getArtist() + " - " + song.getSongName();
            else
            {
                return song.getSongName();
            }
        }catch(Exception e){
            return null;}
    }
    
    /**
     * Change the song/video with x spots in the playlist
     * @param value amount of places in the list it should move
     */
    public void changeSong(int value)
    {
        //Changes the song with x positions in the playlist
        listIndex = listIndex + value;
        if(listIndex < 0)
            listIndex = list.size() - 1;
        if(listIndex > list.size() - 1)
            listIndex = 0;
        
        try{
            stop();
        }catch(Exception e){
            e.printStackTrace();
        }
        play();
    }
    
    /**
     * Select a song/video from the playlist
     * @param value the index of the song that should be played
     */
    public void selectSong(int value)
    {
        //selects the song with index value from the list
        listIndex = value;
        if(listIndex < 0)
            listIndex = list.size() - 1;
        if(listIndex > list.size() - 1)
            listIndex = 0;
        stop();
        play();
    }
    
    /**
     * Removes a song/video at the index
     * @param value index of the song that should be removed
     */
    public void removeSong(int value)
    {
        //Removes at index value
        if(!shuffled && !alphabetical)
        {
            String wantDelete = list.get(value).getPath();
            list.remove(value);
            deleteFromList(shufflelist, wantDelete);
            deleteFromList(alphabeticList, wantDelete);
        }
        else if(shuffled)
        {
            String wantDelete = shufflelist.get(value).getPath();
            shufflelist.remove(value);
            deleteFromList(list, wantDelete);
            deleteFromList(alphabeticList, wantDelete);
        }
        else
        {
            String wantDelete = alphabeticList.get(value).getPath();
            alphabeticList.remove(value);
            deleteFromList(list, wantDelete);
            deleteFromList(shufflelist, wantDelete);
        }
        stop();
        changeSong(0);
        play();
    }
    
    private void deleteFromList(ArrayList<MetaData> li, String wantDelete)
    {
        //searches through a playlist for a song
        try{
            boolean notdone = true;
            for(int i = 0; i < li.size() && notdone; i++)
            {
                if(li.get(i).getPath().equals(wantDelete))
                {
                    li.remove(i);
                    notdone = false;
                }
            }
            wantDelete = null;
        }catch(Exception e){}
    }
    
    /**
     * clear the playlists
     */
    public void clearList()
    {
        //Clears the playlist that the user has in front of him.
        playing = false;
        list = new ArrayList<>();
        shufflelist = new ArrayList<>();
        listIndex = 0;
    }
    
    /**
     * Add a song/video to the playlist
     * @param path path to the file
     */
    public void addSong(String path)
    {
        //A song has been added from the filepath. Makes sure this gets
        //displayed on the interface as well.
        String pathUri = new File(path).toURI().toString();
        try{
            list.add(new MetaData(pathUri)); 
            
            if(shuffled)
                addShuffleSong(pathUri);
            
            if(alphabetical)
                addAlphabeticSong(pathUri);
        }catch(Exception e){System.out.println(e + "addSong");}
    }
    
    private void addAlphabeticSong(String path)
    {
        alphabeticList.add(new MetaData(path));
        sortAlphabetic();
    }
    
    private void addShuffleSong(String path)
    {
        //Checks if the shufflelist already has items in it.
        if(shufflelist.size() <= 1)
            shufflelist.add(new MetaData(path));
        else
        {
            Random rng = new Random();
            shufflelist.add(rng.nextInt(shufflelist.size()), new MetaData(path));
        }
    }
    
    /**
     * stop the mediaplayer
     */
    @Override
    public void stop()
    {
        if(playing)
        {
            mediaplayer.stop();
            mediaplayer.dispose();
            mediaplayer = null;
        }
        
        playing = false;
        paused = false;
    }
    
    /**
     * change the volume of the mediaplayer. Value should be percentage.
     * @param value percentage of volume (100% max, 0% min)
     */
    @Override
    public void setVolume(int value)
    {
        //changes the volume to a percentage/100 (so between 0-1)
        volume = (double)value / 100.0;
        try
        {
            mediaplayer.setVolume(volume);
        }catch(Exception e){}
    }
    
    /**
     * change the volume with a given percentage
     * @param value percentage that should be added/subtracted
     */
    @Override
    public void changeVolume(int value)
    {
        //Changes the volume to a point between 0-1 (1 = 100%)
        volume = mediaplayer.getVolume() + (double)value / 100.0;
        if(volume < 0) volume = 0;
        if(volume > 1) volume = 1;
        
        try
        {
            mediaplayer.setVolume(volume);
        }catch(Exception e){} 
    }
    
    @Override
    public double getVolume()
    {
        return volume;
    }
    
    /**
     * enable/disable shuffling
     */
    @Override
    public void shuffle()
    {
        //checks if it is already shuffled. If so it doesn't shuffle.
        if(!shuffled)
        {
            shuffled = true;
            alphabetical = false;
            shufflelist = new ArrayList<>();
            Random generator = new Random();
            int size = list.size();
            ArrayList<MetaData> templist = new ArrayList<>();
            
            for(int i = 0; i < size; i++)
            {
                templist.add(i, list.get(i));
            }

            for(int i = 0; i < size; i++)
            {
                int index = generator.nextInt(templist.size());
                shufflelist.add(i, templist.get(index));
                templist.remove(index);
            }
        }
        else
        {
            shuffled = false;
            shufflelist = null;
        }
    }
    
    @Override
    public void alphabetical()
    {
        if(!alphabetical || sortStyle < SORTSTYLE_LIMIT)
        {
            if(alphabetical)
                sortStyle++;
            else if(sortStyle == SORTSTYLE_LIMIT)
                sortStyle = BY_SONG;
            if(sortStyle == SORTSTYLE_LIMIT)
            {
                alphabetical = false;
                return;
            }
            alphabetical = true;
            shuffled = false;
            alphabeticList = new ArrayList<>();
            int size = list.size();
            
            for(int i = 0; i < size; i++)
                alphabeticList.add(i, list.get(i));
            
            sortAlphabetic();
        }
        else
            alphabetical = false;
    }
    
    private void sortAlphabetic()
    {
        Collections.sort(alphabeticList, new Comparator<MetaData>(){
            @Override
            public int compare(MetaData m1, MetaData m2){
                String s1;
                String s2;

                if(sortStyle == BY_SONG)
                {
                    s1 = m1.getSongName();
                    s2 = m2.getSongName();
                }
                else if(sortStyle == BY_ARTIST)
                {
                    s1 = m1.getArtist();
                    s2 = m2.getArtist();
                }
                else
                {
                    s1 = m1.getAlbum();
                    s2 = m2.getAlbum();
                }
                return s1.compareToIgnoreCase(s2);
            }
        });
    }
    
    /**
     * pause/unpause the mediaplayer
     * @param stat true is paused, false is unpaused
     */
    @Override
    public void setPaused(boolean stat)
    {
        //Checks wether it should pause or not.
        if(stat)
            mediaplayer.pause();
        else if(mediaplayer != null)
            mediaplayer.play();
        else
            play();
        
        paused = stat;
    }
    
    /**
     * load a playlist at the given path
     * @param listpath path to the playlist
     */
    @Override
    public void loadList(String listpath)
    {
        
        try{
            //loads the playlist (listpath)
            list = new ArrayList<>();
            String line = null;
            FileReader red = new FileReader(listpath);
            BufferedReader bufred = new BufferedReader(red);
            while((line = bufred.readLine()) != null)
            {
                list.add(new MetaData(line));
            }
            
            if(shuffled)
            {
                //makes sure the list can be shuffled
                shuffled = false;
                shuffle();
            }
            if(alphabetical)
            {
                alphabetical = false;
                alphabetical();
            }
               
            red.close();
            bufred.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Get the media that should be played
    /*public String getNext()
    {
        //makes sure the value from the correct playlist is selected.
        if(!shuffled)
            return list.get(listIndex).getPath();
        else
            return shufflelist.get(listIndex).getPath();
    }*/
    
    @Override
    public boolean getShuffled()
    {
        return shuffled;
    }
    
    @Override
    public boolean getAlphabetical()
    {
        return alphabetical;
    }
    
    @Override
    public int getAlphabeticalType()
    {
        return sortStyle;
    }
    
    @Override
    public boolean isPlaying()
    {
        return playing;
    }
    
    @Override
    public boolean isPaused()
    {
        return paused;
    }

    @Override
    public int getIndex() {
        return listIndex;
    }

    @Override
    public int getDuration() {
        return (int) Math.round(mediaplayer.getMedia().getDuration().toSeconds());
    }

    @Override
    public int getPosition() {
        if(mediaplayer == null)
            return -1;
        return (int) Math.round(mediaplayer.getCurrentTime().toSeconds());
    }

    @Override
    public boolean isFinished() {
        return mediaplayer.getMedia().getDuration().equals(mediaplayer.getCurrentTime());
    }

    @Override
    public void seek(int position) {
        System.out.println("SEEK" + position);
        mediaplayer.seek(new Duration(position * 1000));
    }
}
