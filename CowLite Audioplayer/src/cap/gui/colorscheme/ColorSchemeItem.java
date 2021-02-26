/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import static cap.util.SugarySyntax.unwrappedPerform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A ColorSchemeItem is an item in the ColorScheme that can be observed and copied. This base-class
 * implements some default behavior.
 * @author Wessel Jongkind
 * @param <T> The ColorSchemeItem that is extending this class.
 */
public abstract class ColorSchemeItem<T extends ColorSchemeItem> {
    
    // MARK: - Public associated types
    
    public interface Observer<T extends ColorSchemeItem> {
        
        /**
         * Notifies the observer that the ColorSchemeItem has changed. All subclasses of
         * ColorSchemeItem should notify the observer whenever their properties are altered.
         * @param colorSchemeItem The ColorSchemeItem that  was changed.
         */
        public void colorSchemeChanged(T colorSchemeItem);
        
    }
    
    // MARK: - Private properties
    
    private List<WeakReference<Observer<T>>> observers = new ArrayList<>();
    
    // MARK: - Public methods
    
    /**
     * Adds an observer to this ColorSchemeItem.
     * @param observer The observer that needs to be added.
     */
    public void addObserver(Observer<T> observer) {
        observers.add(new WeakReference<>(observer));
    }
    
    // MARK: - Protected methods
    
    /**
     * Notifies all observers that a change has occurred.
     */
    protected void notifyObservers() {
        unwrappedPerform(observers, observer -> observer.colorSchemeChanged((T) this));
    }
    
    /**
     * Makes an exact copy of the ColorSchemeItem.
     * @return A copied instance of the ColorSchemeItem.
     */
    protected abstract T copy();
    
}
