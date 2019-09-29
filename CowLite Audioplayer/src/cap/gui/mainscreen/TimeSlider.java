/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.gui.shared.Slider;
import java.lang.ref.WeakReference;
import cap.gui.colorscheme.ColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;

/**
 *
 * @author Wessel
 */
public class TimeSlider extends Slider {
    
    // MARK: - Associated types & constants
    
    public interface TimeSliderDelegate {
        public void didChangeTrackPosition(double newValue);
    }
    
    // MARK: - Private properties
    
    private WeakReference<TimeSliderDelegate> delegate = new WeakReference<>(null);
    
    // MARK: - Initialisers
    
    public TimeSlider(ColorScheme colorScheme) {
        super(Orientation.horizontal);
        super.setBackground(colorScheme.timeSlider().getBackgroundColor());
        super.setForeground(colorScheme.timeSlider().getFillColor());
        super.setMinimumValue(0);
        super.setMaximumValue(1);
        super.setValue(0);
        super.addChangeListener(e -> didChangePosition());
    }
    
    // MARK: - Getters & Setters
    
    public void setDelegate(TimeSliderDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - ChangeListener
    
    private void didChangePosition() {
        unwrappedPerform(delegate, delegate -> delegate.didChangeTrackPosition(getValue()));
    }
    
}
