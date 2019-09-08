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
 *
 * @author Wessel
 */
public abstract class ColorSchemeItem<T extends ColorSchemeItem> {
    
    // MARK: - Public associated types
    
    public interface Observer<T extends ColorSchemeItem> {
        public void colorSchemeChanged(T colorScheme);
    }
    
    // MARK: - Private properties
    
    private List<WeakReference<Observer<T>>> observers = new ArrayList<>();
    
    // MARK: - Public methods
    
    public void addObserver(Observer<T> observer) {
        observers.add(new WeakReference<>(observer));
    }
    
    // MARK: - Protected methods
    
    protected void notifyObservers() {
        unwrappedPerform(observers, observer -> observer.colorSchemeChanged((T) this));
    }
    
    protected abstract T copy();
    
}
