/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.audio.youtube;

/**
 * Exception that is thrown when a YouTube video is not embeddable.
 * @author Wessel Jongkind
 */
public class VideoNotEmbeddableException extends Exception {
    public VideoNotEmbeddableException() {
        super("Video is not embeddable");
    }
}
