/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import cap.core.ApplicationController;
import cap.core.audio.AudioController;
import cap.core.audio.MetaData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Wessel
 */
public class IO 
{
    private static String docPath;
    public static String getDocumentsFolder() throws Exception
    {
        if(docPath == null)
        {
            JFileChooser fr = new JFileChooser();
            FileSystemView fw = fr.getFileSystemView();
            try{ docPath = fw.getDefaultDirectory().toURI().toURL().toString();}catch(Exception e){}

            docPath = docPath.replace("file:/", "");
            docPath = docPath.replace("/", "\\");
        }
        
        return docPath + "CowLite Audioplayer";
    }
    
    public static void saveFile(AudioController controller, String name) throws Exception
    {
        String path = getDocumentsFolder() + "\\resources\\playlists\\" + name;
        if(controller.playlistExists(name, path))
            throw new Exception("Playlistname not unique");

        PrintStream out = new PrintStream(new FileOutputStream(new File(path)));

        List<MetaData> playlist = (List<MetaData>)controller.getPlayer().getList();
        for(int i = 0; i < playlist.size(); i++)
            out.println(playlist.get(i).getPath());

        FileReader red = new FileReader(getDocumentsFolder() + "\\resources\\launchersettings\\savedlists.txt");
        BufferedReader bufred = new BufferedReader(red);
        String line = null;
        ArrayList<String> playlists = new ArrayList<>();
        playlists.add(name + "=:" +path);
        controller.addPlaylist(name, path);
        String empty = bufred.readLine();
        while((line = bufred.readLine()) != null)
        {
            if(!line.equals(path))
                playlists.add(line);
        }
        out = new PrintStream(new FileOutputStream(new File(getDocumentsFolder() + "\\resources\\launchersettings\\savedlists.txt")));

        out.println(empty);
        for(int i = 0; i < playlists.size(); i++)
        {
            out.println(playlists.get(i));
        }
        out.close();
    }
    
    public static void importYoutube(AudioController controller, String id, String name) throws Exception
    {
        if(controller.playlistExists(name, id))
            throw new Exception("Playlist already exists!");
            
        FileReader red = new FileReader(getDocumentsFolder() + "\\resources\\launchersettings\\savedlists.txt");
        BufferedReader bufred = new BufferedReader(red);

        String line = null;
        ArrayList<String> playlists = new ArrayList<>();
        playlists.add(name + "=:" + id);
        controller.addPlaylist(name, id);
        
        while((line = bufred.readLine()) != null)
        {
            if(!line.equals(name + "=:" + id))
                playlists.add(line);
        }

        PrintStream out = new PrintStream(new FileOutputStream(new File(getDocumentsFolder() + "\\resources\\launchersettings\\savedlists.txt")));

        for(int i = 0; i < playlists.size(); i++)
            out.println(playlists.get(i));

        out.close();
        controller.loadPlaylist(name);
    }
    
    /**
     * Remove a playlist file
     */
    public static void removePlaylist(AudioController audio, ApplicationController controller, String name)
    {
        String path = audio.getPlaylistPath(name);
        File f = new File(path);
        
        while(f.exists())
            f.delete();

        audio.getPlayer().stop();
        audio.getPlayer().clearList();

        try
        {
            audio.removePlaylist(name);
            PrintStream out = new PrintStream(new FileOutputStream(new File(getDocumentsFolder() + "\\resources\\launchersettings\\savedlists.txt")));

            Map<String, String> playlists = audio.getPlaylists();
            for(Map.Entry<String, String> entry : playlists.entrySet())
                System.out.println(entry.getKey() + "=:" + entry.getValue());
            
            out.close();
        }catch(Exception g){}
        controller.stopEvent();
    }
    
    public static void main(String[] args) throws Exception
    {
        System.out.println(getDocumentsFolder());
    }
}
