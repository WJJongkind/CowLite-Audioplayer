/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import static cap.util.SugarySyntax.doTry;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Wessel
 */
public final class ActivityQueue extends Thread {
    
    // MARK: - Associated types & constants
    
    public interface Activity {
        public void perform();
    }
    
    public interface NotifyOnCancelActivity extends Activity {
        public void didCancel();
    }
    
    public static final class ActivityToken {
        
        // MARK: - Private associated types
        
        private enum State {
            scheduled,
            running,
            done
        }
        
        // MARK: - Private properties
        
        private final Activity activity;
        
        private State state = State.scheduled;
        
        // MARK: - Initialisers
        
        private ActivityToken(Activity activity) {
            this.activity = activity;
        }
        
        // MARK: - Public methods
        
        public boolean cancelActivity() {
            boolean isCancellable;
            
            synchronized(state) {
                isCancellable = state == State.scheduled;
                
                if(!isCancellable) {
                    return false;
                }
                
                state = State.done;
            }

            if(activity instanceof NotifyOnCancelActivity) {
                ((NotifyOnCancelActivity) activity).didCancel();
            }

            return true;
        }
        
        public boolean isActivityValid() {
            synchronized(state) {
                return state == State.done;
            }
        }
        
        // MARK: - Private methods
        
        private void performActivity() {
            synchronized(state) {
                if(state == State.scheduled) {
                    state = State.running;
                }
            }

            if(state == State.running) {
                activity.perform();
            }

            synchronized(state) {
                state = State.done;
            }
        }
        
    }
    
    // MARK: - Private properties
    
    private final LinkedBlockingDeque<ActivityToken> activityQueue = new LinkedBlockingDeque<>();
    
    // MARK: - Thread

    @Override
    public void run() {
        while(true) {
            doTry(() -> {
                ActivityToken token = activityQueue.poll(Long.MAX_VALUE, TimeUnit.DAYS);
                token.performActivity();
            });
        }
    }
    
    // MARK: - Public methods
    
    public synchronized ActivityToken submitActivity(Activity activity) {
        ActivityToken token = new ActivityToken(activity);
        activityQueue.add(token);
        
        return token;
    }
    
}
