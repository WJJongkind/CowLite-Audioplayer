/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core.audio.youtube;

import cap.core.audio.AudioPlayer;
import com.google.api.services.youtube.model.PlaylistItem;
import com.teamdev.jxbrowser.chromium.BeforeSendHeadersParams;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSArray;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MemeMeister
 */
public class YTAudioPlayer implements AudioPlayer
{
    private String html;
    private Browser player;
    private Map<String, PlaylistItem> videodata;
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
                        PLAY_INDEX = new Command("player.playVideoAt"),
                        GET_VOLUME = new Command("player.getVolume"),
                        SHUFFLE = new Command("player.setShuffle"),
                        GET_INDEX = new Command("player.getPlaylistIndex"),
                        GET_DURATION = new Command("player.getDuration");
    
    private final Map<String, String> SETTINGS;
    
    public YTAudioPlayer(Map<String, String> settings)
    {
        this.SETTINGS = settings;
    }
    
    private JSValue executeCommand(Command command) 
    {
        try{
            JSValue result = player.executeJavaScriptAndReturnValue(command.toString());
            command.clearParameters();
            return result;
        }catch(Exception e){
            //e.printStackTrace();
            return null;
        }
    }

    @Override
    public void play() {
        executeCommand(PLAY);
        playing = true;
    }

    private JSValue previousResult;
    @Override
    public List<String> getList() {
        JSValue result = executeCommand(GET_LIST);
        previousResult = result;
        
        JSArray list = result.asArray();
        ArrayList<String> playlist = new ArrayList<>();
        for(int i = 0; i < list.length(); i++)
        {
            playlist.add(videodata.get(list.get(i).asString().getStringValue()).getSnippet().getTitle());
        }
       // return Arrays.asList(result.asArray()..split(","));
       return playlist;
    }

    @Override
    public String getSongInfo() {
        return getList().get(getIndex());
    }

    @Override
    public void changeSong(int value) {
        if(value == 1)
            executeCommand(NEXT);
        else
            executeCommand(PREVIOUS);
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
        html = null;
        player = null;
        playing = false;
        paused = false;
    }

    @Override
    public void stop() {
        executeCommand(STOP);
        playing = false;
    }

    @Override
    public void setVolume(int value) {
        SETTINGS.replace("volume", value + "");
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
        return executeCommand(GET_VOLUME).asNumber().getInteger() / 100.0;
    }

    @Override
    public void shuffle() {
        shuffled = !shuffled;
        SHUFFLE.addParameter(shuffled);
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
        "<iframe id=\"player\" width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/videoseries?list=" + list + "&enablejsapi=1\" frameborder=\"0\" gesture=\"media\" allowfullscreen></iframe>\n" +
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
        "      player.setVolume(volume); " +
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
        
        try{
            System.out.println("GETTING DATA FOR: " + list);
            videodata = YouTubePlaylistLoader.getYouTubePlaylistVideos(list);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        player = new Browser();
        
        //Setup the refere header so that YouTube WMG does not block videos
        player.getContext().getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate(){
            @Override
            public void onBeforeSendHeaders(BeforeSendHeadersParams params)
            {
                 params.getHeadersEx().setHeader("Referer", "http://www.cowlite.nl");
            }
        });
        player.loadHTML(html);
        playing = true;
        System.out.println(SETTINGS.get("volume"));
        setVolume(Integer.parseInt(SETTINGS.get("volume")));
    }

    @Override
    public int getIndex() {
        return executeCommand(GET_INDEX).asNumber().getInteger();
    }

    @Override
    public int getDuration() {
        return executeCommand(GET_DURATION).asNumber().getInteger();
    }

    @Override
    public int getPosition() {
        return executeCommand(GET_POSITION).asNumber().getInteger();
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
