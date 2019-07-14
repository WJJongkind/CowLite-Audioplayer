/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 *
 * @author Wessel
 */
public class SugarySyntax {
    
    public static <T> T nilCoalesce(T primary, T secondary) {
        return primary != null ? primary : secondary;
    }
    
    public static <T> void unwrappedPerform(WeakReference<T> target, TargetedAction<T> action) {
        if(target == null) {
            return;
        }
        
        T strongDelegate = target.get();
        if(strongDelegate != null) {
            action.perform(strongDelegate);
        }
    }
    
    public static <T> void unwrappedPerform(Collection<WeakReference<T>> targets, TargetedAction<T> action) {
        for(WeakReference<T> target : targets) {
            unwrappedPerform(target, action);
        }
    }
    
    public interface TargetedAction<T> {
        public void perform(T delegate);
    }
}
