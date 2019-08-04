/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.gui.shared.Slider;
import java.lang.ref.WeakReference;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class TimeSlider extends Slider {
    
    // MARK: - Associated types & constants
    
    public interface TrackPositionDelegate {
        public void didChangeTrackPosition(double newValue);
    }
    
    // MARK: - Private properties
    
    private WeakReference<TrackPositionDelegate> delegate = new WeakReference<>(null);
    
    public TimeSlider(ColorScheme colorScheme) {
        super(Orientation.horizontal);
        super.setBackground(colorScheme.timeSliderColor().backgroundColor());
        super.setForeground(colorScheme.timeSliderColor().fillColor());
        super.setMinimumValue(0);
        super.setMaximumValue(1);
        super.setValue(0);
        super.addChangeListener(e -> didChangePosition());
    }
    
    // MARK: - Getters & Setters
    
    public void setDelegate(TrackPositionDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - ChangeListener
    
    private void didChangePosition() {
        TrackPositionDelegate strongDelegate = delegate.get();
        if(strongDelegate != null) {
            strongDelegate.didChangeTrackPosition(getValue());
        }
    }
    
}
