package cap.core.audio;

import cap.core.CoreTime;
import cap.core.CowLiteAudioPlayer;
import cap.gui.GUIHandler;
import cap.gui.GraphicalInterface;
import cap.util.InterfaceIO;
import java.io.BufferedReader;
import javafx.scene.media.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
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
    
    
    /**
     * retreives the settings for audioplayer and instantiates itself
     */
    public FileAudioPlayer()
    {
        //Instantiates the audioplayer
        ArrayList<String> settings = new ArrayList<>();
        playing = false;
        paused = false;
        
        try
        {
            //Gathers it's required settings
            String line = null;
            FileReader red = new FileReader(CowLiteAudioPlayer.docPath + "\\CowLite Audio Player\\resources\\launchersettings\\LaunchSettings.txt");
            BufferedReader bufred = new BufferedReader(red);
            while((line = bufred.readLine()) != null)
            {
                settings.add(line);
            }
            red.close();
            bufred.close();
            
            //Sets the settings relevant to this class
            volume = Double.parseDouble(settings.get(1));
            GraphicalInterface.volumeSlider.setValue((int)(volume * 100));
            shuffled = Boolean.parseBoolean(settings.get(2));
            alphabetical = Boolean.parseBoolean(settings.get(3));
            sortStyle = Integer.parseInt(settings.get(4));
            
            //shuffled = false;
            //alphabetical = true;
            if(shuffled)
                InterfaceIO.setShufflePressed();
            if(alphabetical)
                InterfaceIO.setAlphabeticPressed();
            listIndex = 0;
        }catch(Exception e){e.printStackTrace();}
    }

    //Starts playing a song/video at selected index
    public void play()
    {
        try
        {
            if(getList() == null || getList().size() == 0)
                loadList(CowLiteAudioPlayer.playlists.get(1));
            //Displays on the GUI which song/file is going to be played
            GraphicalInterface.songlist.setSelectedIndex(listIndex);
            GraphicalInterface.songlist.ensureIndexIsVisible(GraphicalInterface.songlist.getSelectedIndex());
            GUIHandler.frame.repaint();
            
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
            media = null;
            InterfaceIO.setPauseButton();
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
        
        //Makes sure the frame gets updated accordingly
        GraphicalInterface.songlist.setSelectedIndex(-1);
        GraphicalInterface.uptodate = false;
        CoreTime.update = true;
    }
    
    /**
     * Add a song/video to the playlist
     * @param path path to the file
     */
    public void addSong(String path)
    {
        //A song has been added from the filepath. Makes sure this gets
        //displayed on the interface as well.
        GraphicalInterface.uptodate = false;
        String pathUri = new File(path).toURI().toString();
        try{
            list.add(new MetaData(pathUri)); 
            addShuffleSong(pathUri);
            addAlphabeticSong(pathUri);
        }catch(Exception e){System.out.println(e + "addSong");}
        CoreTime.update = true;
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
    public void stop()
    {
        //sets boolean so that other classes know it's not playing.
        playing = false;
        paused = false;
        try
        {
            mediaplayer.stop();
            mediaplayer.dispose();
            mediaplayer = null;
        }catch(Exception e){}
        
        InterfaceIO.setPlayButton();
    }
    
    /**
     * change the volume of the mediaplayer. Value should be percentage.
     * @param value percentage of volume (100% max, 0% min)
     */
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
    public void changeVolume(int value)
    {
        //Changes the volume to a point between 0-1 (1 = 100%)
        volume = mediaplayer.getVolume() + (double)value / 100.0;
        if(volume < 0) volume = 0;
        if(volume > 1) volume = 1;
        GraphicalInterface.volumeSlider.setValue((int)(volume * 100));
        try
        {
            mediaplayer.setVolume(volume);
        }catch(Exception e){} 
    }
    
    public double getVolume()
    {
        return volume;
    }
    
    /**
     * enable/disable shuffling
     */
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
            GraphicalInterface.uptodate = false;
        }
        else
        {
            shuffled = false;
            shufflelist = null;
            GraphicalInterface.uptodate = false;
        }
        CoreTime.update = true;
    }
    
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
                GraphicalInterface.uptodate = false;
                CoreTime.update = true;
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
        GraphicalInterface.uptodate = false;
        CoreTime.update = true;
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
    public void setPaused(boolean stat)
    {
        //Checks wether it should pause or not.
        if(stat)
        {
            paused = true;
            mediaplayer.pause();
            InterfaceIO.setPlayButton();
        }

        else
        {
            paused = false;
            mediaplayer.play();
            InterfaceIO.setPauseButton();
        }
    }
    
    /**
     * load a playlist at the given path
     * @param listpath path to the playlist
     */
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
        }catch(Exception e){e.printStackTrace();}
        GraphicalInterface.uptodate = false;
        CoreTime.update = true;
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
    
    public boolean getShuffled()
    {
        return shuffled;
    }
    
    public boolean getAlphabetical()
    {
        return alphabetical;
    }
    
    public int getAlphabeticalType()
    {
        return sortStyle;
    }
    
    public boolean isPlaying()
    {
        return playing;
    }
    
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
        return (int) Math.round(mediaplayer.getCurrentTime().toSeconds());
    }

    @Override
    public boolean isFinished() {
        return mediaplayer.getMedia().getDuration().equals(mediaplayer.getCurrentTime());
    }

    @Override
    public void seek(int position) {
        mediaplayer.seek(new Duration(position * 1000));
    }
}
