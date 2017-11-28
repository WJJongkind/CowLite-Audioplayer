/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.util.IO;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Wessel
 */
public class PropertiesManager 
{
    private final HashMap<String, String> PLAYLISTS;
    private final HashMap<String, Object> GRAPHICS;
    private final HashMap<String, String> CONTROLS;
    private final HashMap<String, String> AUDIO;
    
    public PropertiesManager() throws Exception
    {
        PLAYLISTS = new HashMap<>();
        GRAPHICS = new HashMap<>();
        CONTROLS = new HashMap<>();
        AUDIO = new HashMap<>();
        loadGraphics();
        standardLoad(PLAYLISTS, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt");
        System.out.println("PLAYLISTS: " + PLAYLISTS.size());
        standardLoad(CONTROLS, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\controls.txt");
        standardLoad(AUDIO, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\audiosettings.txt");
    }
    
    private void standardLoad(Map<String, String> target, String path)
    {
        try{
            //Gets all the graphics values from the settings file. Uses
            //regex to see what the settings are.
            String regex = "([a-zA-Z ]*)=:(.*)";
            Pattern pattern = Pattern.compile(regex);
            
            Matcher matcher;
            String line = null;
            FileReader red = new FileReader(new File(path));
            BufferedReader bufred = new BufferedReader(red);
            while((line = bufred.readLine()) != null)
            {
                matcher = pattern.matcher(line);
                if(matcher.matches())
                    target.put(matcher.group(1), matcher.group(2));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void loadGraphics()
    {
        try{
            
            //Gets all the graphics values from the settings file. Uses
            //regex to see what the settings are.
            String colorRegex = "([a-zA-Z]*)=:java.awt.Color\\[r=(\\d*),g=(\\d*),b=(\\d*)\\]";
            Pattern colorPattern = Pattern.compile(colorRegex);
            
            String windowRegex = "([a-zA-Z]*)=:(.*)";
            Pattern windowPattern = Pattern.compile(windowRegex);
            
            Matcher matcher;
            String line = null;
            FileReader red = new FileReader(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\graphicssettings.txt"));
            BufferedReader bufred = new BufferedReader(red);
            while((line = bufred.readLine()) != null)
            {
                matcher = colorPattern.matcher(line);
                if(matcher.matches())
                {
                    GRAPHICS.put(matcher.group(1), new Color(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
                    continue;
                }
                
                matcher = windowPattern.matcher(line);
                
                try{
                if(matcher.matches())
                    GRAPHICS.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
                }catch(Exception exp){
                    
                if(matcher.matches())
                    GRAPHICS.put(matcher.group(1), matcher.group(2));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Map<String, String> getAudioProperties()
    {
        return AUDIO;
    }
    
    public Map<String, String> getControlProperties()
    {
        return CONTROLS;
    }
    
    public Map<String, Object> getGraphicsProperties()
    {
        return GRAPHICS;
    }
    
    public Map<String, String> getPlaylists()
    {
        return PLAYLISTS;
    }
    
    public void storeProperties(Map<String, ?> properties)
    {
        try{
            String path = null;
            if(properties.equals(PLAYLISTS))
                path = IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\savedlists.txt";
            if(properties.equals(CONTROLS))
                path = IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\controls.txt";
            if(properties.equals(AUDIO))
                path = IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\audiosettings.txt";
            if(properties.equals(GRAPHICS))
                path = IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\graphicssettings.txt";
            
            PrintStream out = new PrintStream(new FileOutputStream(new File(path)));
            for(Map.Entry<String, ?> entry: properties.entrySet())
                out.println(entry.getKey() + "=:" + entry.getValue());
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
