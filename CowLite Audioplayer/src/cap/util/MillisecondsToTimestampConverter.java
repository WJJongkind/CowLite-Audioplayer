/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

/**
 *
 * @author Wessel
 */
public class MillisecondsToTimestampConverter {
    
    public static final String convert(long milliseconds) {
        long minutes = milliseconds / 60000;
        long seconds = (milliseconds % 60000) / 1000;
        
        String minutesString = minutes < 10 ? "0" + minutes : minutes + "";
        String secondsString = seconds < 10 ? "0" + seconds : seconds + "";
        
        return minutesString + ":" + secondsString;
    }
    
}
