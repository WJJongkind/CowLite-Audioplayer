/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.gui.shared.Slider;
import java.lang.ref.WeakReference;
import cap.gui.colorscheme.UILayout;

/**
 *
 * @author Wessel
 */
public class VolumeSlider extends Slider {
    
    // MARK: - Associated types & constants
    
    public interface VolumeSliderDelegate {
        public void didChangeVolume(double newValue);
    }
    
    // MARK: - Private properties
    
    private WeakReference<VolumeSliderDelegate> delegate = new WeakReference<>(null);
    
    // MARK: - Initialisers
    
    public VolumeSlider(UILayout colorScheme) {
        super(Orientation.vertical);
        super.setBackground(colorScheme.volumeColor().backgroundColor());
        super.setForeground(colorScheme.volumeColor().fillColor());
        super.setMinimumValue(0);
        super.setMaximumValue(1);
        super.setValue(0.5);
        super.addChangeListener(e -> didChangeVolume());
    }
    
    // MARK: - Getters & Setters
    
    public void setDelegate(VolumeSliderDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - ChangeListener
    
    private void didChangeVolume() {
        VolumeSliderDelegate strongDelegate = delegate.get();
        if(strongDelegate != null) {
            strongDelegate.didChangeVolume(getValue());
        }
    }
}
