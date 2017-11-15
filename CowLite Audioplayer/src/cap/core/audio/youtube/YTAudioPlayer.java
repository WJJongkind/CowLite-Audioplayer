/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio.youtube;

import cap.core.CoreTime;
import cap.core.audio.AudioPlayer;
import cap.gui.GraphicalInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.JFrame;

/**
 *
 * @author MemeMeister
 */
public class YTAudioPlayer implements AudioPlayer
{
    private String html;
    private WebView player;
    private JFXPanel jfxPanel;
    private boolean shuffled = false, playing = false, paused = false;
    
    private static final Command NEXT = new Command("player.nextVideo"), 
                        PREVIOUS = new Command("player.previousVideo"), 
                        SETVOLUME = new Command("setVolume"),
                        PAUSE = new Command("player.pauseVideo"), 
                        STOP = new Command("player.stopVideo"), 
                        SEEK = new Command("player.seekTo"){
                            @Override
                            public String toString()
                            {
                                addParameter(true);
                                return super.toString();
                            }
                        },
                        GET_POSITION = new Command("player.getCurrentTime"),
                        PLAY = new Command("player.playVideo"),
                        GET_LIST = new Command("player.getPlaylist"),
                        GET_TITLE = new Command("player.getVideoData().title"){
                            @Override
                            public String toString()
                            {
                                return "player.getVideoData().title";
                            }
                        },
                        PLAY_INDEX = new Command("player.playVideoAt"),
                        GET_VOLUME = new Command("player.getVolume"),
                        SHUFFLE = new Command("player.setShuffle"),
                        GET_INDEX = new Command("player.getPlaylistIndex"),
                        GET_DURATION = new Command("player.getDuration");
    
    private String result;
    public String executeCommand(Command command) 
    {
        try{
            Semaphore semaphore = new Semaphore(0);
            Platform.runLater ( new Runnable () {
                @Override
                public void run () {
                    try{
                        result = player.getEngine().executeScript(command.toString()).toString();
                        semaphore.release();
                    }catch(Exception e){
                        System.out.println(command.toString());
                        e.printStackTrace();
                        result = null;
                        semaphore.release();
                    }
                }
            } );
            semaphore.acquire();
            command.clearParameters();
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void play() {
        executeCommand(PLAY);
        playing = true;
    }

    private String previousResult;
    @Override
    public List<String> getList() {
        String result = executeCommand(GET_LIST);
        if(result.equals(previousResult))
            GraphicalInterface.uptodate = true;
        previousResult = result;
        return Arrays.asList(result.split(","));
    }

    @Override
    public String getSongInfo() {
        return executeCommand(GET_TITLE);
    }

    @Override
    public void changeSong(int value) {
        executeCommand(NEXT);
    }

    @Override
    public void selectSong(int value) {
        PLAY_INDEX.addParameter(value);
        executeCommand(PLAY_INDEX);
    }

    @Override
    public void removeSong(int value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearList() {
        stop();
        player = null;
        jfxPanel = null;
        html = null;
        playing = false;
        paused = false;
        GraphicalInterface.songlist.setSelectedIndex(-1);
        GraphicalInterface.uptodate = false;
        CoreTime.update = true;
    }

    @Override
    public void stop() {
        executeCommand(STOP);
        playing = false;
    }

    @Override
    public void setVolume(int value) {
        SETVOLUME.addParameter(value);
        executeCommand(SETVOLUME);
    }

    @Override
    public void changeVolume(int value)
    {
        setVolume((int)Math.round(getVolume() * 100) + value);
    }

    @Override
    public double getVolume() {
        return Double.parseDouble(executeCommand(GET_VOLUME)) / 100;
    }

    @Override
    public void shuffle() {
        executeCommand(SHUFFLE);
    }

    @Override
    public void alphabetical() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPaused(boolean stat) {
        if(stat)
            executeCommand(PAUSE);
        else
            executeCommand(PLAY);
        this.paused = stat;
    }

    @Override
    public boolean getShuffled() {
        return shuffled;
    }

    @Override
    public boolean getAlphabetical() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getAlphabeticalType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void loadList(String list) 
    {
        html = "<!DOCTYPE html>\n" +
        "<html>\n" +
        "  <body>\n" +
        "<iframe id=\"player\" width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/videoseries?list=PL7aXwAD1wk5kdkuQOFBHY63R9Bhki7rCg&enablejsapi=1\" frameborder=\"0\" gesture=\"media\" allowfullscreen></iframe>\n" +
        "\n" +
        "<script type=\"text/javascript\">\n" +
        "  var tag = document.createElement('script');\n" +
        "  tag.id = 'iframe-demo';\n" +
        "  tag.src = 'https://www.youtube.com/iframe_api';\n" +
        "  var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
        "  var volume = 100;\n" +
        "  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
        "\n" +
        "  var player;\n" +
        "  function onYouTubeIframeAPIReady() {\n" +
        "    player = new YT.Player('player', {\n" +
        "        events: {\n" +
        "          'onReady': onPlayerReady,\n" +
        "          'onError' : handleError,\n" +
        "          'onStateChange' : stateChange,\n" +
        "          'onApiChange' : stateChange\n" +
        "        }\n" +
        "    });\n" +
        "  }\n" +
        "  function onPlayerReady(event) {\n" +
        "     event.target.playVideo();\n" +
        "      //event.target.nextVideo();\n" +
        "  }\n" +
        "    \n" +
        "    function handleError(event)\n" +
        "    {\n" +
        "        console.log(event);\n" +
        "        player.nextVideo();\n" +
        "    }\n" +
        "    \n" +
        "    function setVolume(vol)\n" +
        "    {\n" +
        "        volume = vol;\n" +
        "        player.setVolume(vol);\n" +
        "    }\n" +
        "    \n" +
        "    function stateChange(event)\n" +
        "    {\n" +
        "       // player.setVolume(volume + 1);\n" +
        "       // player.setVolume(volume - 1);\n" +
        "      //  player.setVolume(volume);\n" +
        "    }\n" +
        "</script>\n" +
        "  </body>\n" +
        "</html>";
        
        jfxPanel = new JFXPanel();
        Platform.runLater ( new Runnable () {
            @Override
            public void run () {
                player = new WebView();
                
                player.getEngine().setJavaScriptEnabled(true);
                player.setCache(true);
                player.setContextMenuEnabled(true);
                player.getEngine().loadContent(html);
                jfxPanel.setScene(new Scene(player));
                JFrame f = new JFrame();
                f.add(jfxPanel);
                f.setSize(1280,720);
                f.setVisible(true);
                
            }
        } );
        GraphicalInterface.uptodate = false;
    }

    @Override
    public int getIndex() {
        return Integer.parseInt(executeCommand(GET_INDEX));
    }

    @Override
    public int getDuration() {
        return (int)Math.round(Double.parseDouble(executeCommand(GET_DURATION)));
    }

    @Override
    public int getPosition() {
        return (int)Math.round(Double.parseDouble(executeCommand(GET_POSITION)));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void seek(int position) {
        SEEK.addParameter(position);
        executeCommand(SEEK);
    }
    
    private static class Command 
    {
        private final String targetFunction;
        private final ArrayList<Object> parameters = new ArrayList<>();

        public Command(String targetFunction)
        {
            this.targetFunction = targetFunction;
        }

        public void addParameter(Object parameter)
        {
            parameters.add(parameter);
        }
        
        private void clearParameters()
        {
            parameters.clear();
        }

        @Override
        public String toString()
        {
            String command = targetFunction + "(";
            for(Object object : parameters)
            {
                command += object + ", ";
            }

            if(!parameters.isEmpty())
                command = command.substring(0, command.length() - 2);

            return command + ")";
        }
    }
}
