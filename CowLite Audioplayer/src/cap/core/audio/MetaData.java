/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio;

import cap.util.CommonMethods;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import java.net.URL;
import java.net.URLDecoder;

/**
 *
 * @author Wessel
 */
public class MetaData 
{
    private String artist, album, songname, path;
    private boolean found;
    public static final String UNKNOWN = "Unknown", SONGNAME = "songname", ARTIST = "artist", ALBUM = "album";
    
    public MetaData(String path)
    {
        this.path = path;
        this.found = true;
        
        ID3v1 tag = getTag(path);
        String artist = getTagElement(tag, ARTIST);
        if(artist != null)
            this.artist = artist;
        else
            this.artist = UNKNOWN;

        String songname = getTagElement(tag, SONGNAME);
        if(songname != null)
            this.songname = songname;
        else
        {
            found = false;
            this.songname = CommonMethods.pathToFilename(path);
        }

        String album = getTagElement(tag, ALBUM);
        if(album != null)
            this.album = album;
        else
            this.album = UNKNOWN;
        //albums.add(getAlbum(metadata));
        this.songname  = filterData(this.songname);
        this.album = filterData(this.album);
        this.artist = filterData(this.artist);
        System.out.println("SONGNAME: " + this.songname + "@@@ALBUM: " + this.album);
    }
    
    private String filterData(String raw)
    {
        try{
        raw = raw.trim();
        raw = URLDecoder.decode(raw, "UTF-8");
        if(raw.equals(""))
            return UNKNOWN;
        else
            return raw;
        }catch(Exception e){return UNKNOWN;}
        /*try{
        System.out.println(raw);
        Pattern pattern = Pattern.compile("(\\s*[\\w\\.\\-\\_\\(\\)\\[\\]]+\\s*)+");
        Matcher m = pattern.matcher(raw);
        if(m.matches())
            return raw;
        else
            return UNKNOWN;
        }catch(Exception e){e.printStackTrace();return null;}*/
    }
    
    private ID3v1 getTag(String path)
    {
        Mp3File song;
        String uri = "";
        try{
            uri = new URL(path).toURI().toString();
            uri = uri.toLowerCase().replace("%20", " ");
            uri = uri.replace("file:/", "");
            song = new Mp3File(uri);
        }catch(Exception e){
            return null;
        }
        
        ID3v1 tags;
        if(song.hasId3v2Tag())
             return song.getId3v2Tag();
        else if(song.hasId3v1Tag())
            return song.getId3v1Tag();
        else
            return null;
    }
    
    private String getTagElement(ID3v1 tags, String tag)
    {
        if(tags == null)
            return null;
        if(tag.equals(ARTIST))
            return tags.getArtist();
        if(tag.equals(ALBUM))
            return tags.getAlbum();
        else
            return tags.getTitle();
    }
    
    public String getArtist()
    {
        return artist;
    }
    
    public String getSongName()
    {
        return songname;
    }
    
    public String getAlbum()
    {
        return album;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public boolean getDataFound()
    {
        return found;
    }
    
    /*
    
            
    */
}
