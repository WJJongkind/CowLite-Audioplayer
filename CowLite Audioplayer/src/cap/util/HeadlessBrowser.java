/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import com.sun.javafx.webkit.WebConsoleListener;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Wessel
 */
public class HeadlessBrowser {
    
    public interface SimpleConsoleListener {
        
        public void receivedMessage(String message);
        
    }
    
    private WebEngine webEngine;
    
    public HeadlessBrowser() {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            
            Platform.runLater(() -> {
                webEngine = new WebEngine();
                latch.countDown();
            });
            
            latch.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public <T> T executeJavaScript(String javaScript) {
        try {
            ResultCarryingCountdownLatch<T> latch = new ResultCarryingCountdownLatch<>(1);
            
            Platform.runLater(() -> {
                try {
                    latch.countDown((T) webEngine.executeScript(javaScript));
                } catch(Exception e) {
                    latch.countDown(null);
                }
            });
            
            return latch.awaitResult();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    // TODO in the future... somewhere... maybe... This will block threads prepretually if there's a timeout maybe?
    public boolean loadWebPage(String url) {
        try {
            ResultCarryingCountdownLatch<Boolean> latch = new ResultCarryingCountdownLatch<>(1);
            ChangeListener webEngineStateListener = new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldState, Object newState) {
                    if(newState == Worker.State.SUCCEEDED) {
                        latch.countDown(true);
                        observable.removeListener(this);
                    } else if(newState == Worker.State.CANCELLED || newState == Worker.State.FAILED) {
                        latch.countDown(false);
                        observable.removeListener(this);
                    }
                }
            };
            
            Platform.runLater(() -> {
                webEngine.getLoadWorker().stateProperty().addListener(webEngineStateListener);
                webEngine.load(url);
            });
            
            return latch.awaitResult();
        } catch (InterruptedException ex) {
            return false;
        }
    }
    
    public void setConsoleListener(WebConsoleListener listener) {
        WebConsoleListener.setDefaultListener(listener);
    }
    
    public void setConsoleListener(SimpleConsoleListener listener) {
        WebConsoleListener.setDefaultListener(new WebConsoleListener() {
            @Override
            public void messageAdded(WebView webView, String message, int line, String sourceId) {
                listener.receivedMessage(message);
            }
        });
    }
    
}
