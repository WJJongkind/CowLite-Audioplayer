/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.ControlImageSet;
import cap.gui.colorscheme.GUIImageSet;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.lang.ref.WeakReference;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author Wessel
 */
public class MusicControlPane extends JPanel {
    
    // MARK: - Associated types
    
    public interface MusicControlPaneDelegate {
        public boolean didPressPlayButton(MusicControlPane sender);
        public void didPressPauseButton(MusicControlPane sender);
        public void didPressPreviousButton(MusicControlPane sender);
        public void didPressNextButton(MusicControlPane sender);
        public void didPressStopButton(MusicControlPane sender);
        public boolean didPressShuffleButton(MusicControlPane sender);
        public boolean didPressAlphabeticSortButton(MusicControlPane sender);
        public void didPressClearButton(MusicControlPane sender);
    }
    
    // MARK: - Layout properties
    
    private static final class Layout {
        static final Dimension playButtonSize = new Dimension(15, 15);
        static final Dimension pauseButtonSize = new Dimension(15, 15);
        static final Dimension stopButtonSize = new Dimension(15, 15);
        static final Dimension nextButtonSize = new Dimension(15, 15);
        static final Dimension previousButtonSize = new Dimension(15, 15);
        static final Dimension shuffleButtonSize = new Dimension(19, 15);
        static final Dimension clearButtonSize = new Dimension(30, 15);
        static final Dimension alphabeticButtonSize = new Dimension(29, 15);
        static final int spacingBetweenButtons = 5;
    }
    
    // MARK: - Private properties
    
    private WeakReference<MusicControlPaneDelegate> delegate;
    private GUIImageSet imageSet;
    
    private JToggleButton playButton, 
                          pauseButton,
                          stopButton,
                          prevButton, 
                          nextButton, 
                          clearButton, 
                          shuffleButton, 
                          alphabeticButton;
    
    public MusicControlPane(ColorScheme colorScheme) {
        imageSet = colorScheme.imageSet();
        makeButtons(colorScheme);
        
        super.setBackground(colorScheme.frameColor());
        super.setLayout(new FlowLayout(FlowLayout.LEFT, Layout.spacingBetweenButtons, 0));
        super.add(prevButton);
        super.add(playButton);
        super.add(pauseButton);
        super.add(stopButton);
        super.add(nextButton);
        super.add(shuffleButton);
        super.add(alphabeticButton);
        super.add(clearButton);
        
        pauseButton.setVisible(false);
    }
    
    // MARK: - Getters & setters
    
    public void setDelegate(MusicControlPaneDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - Private methods
    
    private void makeButtons(ColorScheme colorScheme) {
        playButton = makeButton(colorScheme.imageSet().playButton(), Layout.playButtonSize);
        playButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> didPressPlay(delegate)));
        
        pauseButton = makeButton(colorScheme.imageSet().pauseButton(), Layout.pauseButtonSize);
        pauseButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressPauseButton(this)));
        
        stopButton = makeButton(colorScheme.imageSet().stopButton(), Layout.stopButtonSize);
        stopButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressStopButton(this)));
        
        nextButton = makeButton(colorScheme.imageSet().nextButton(), Layout.nextButtonSize);
        nextButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressNextButton(this)));
        
        prevButton = makeButton(colorScheme.imageSet().previousButton(), Layout.previousButtonSize);
        prevButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressPreviousButton(this)));
        
        clearButton = makeButton(colorScheme.imageSet().clearButton(), Layout.clearButtonSize);
        clearButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> delegate.didPressClearButton(this)));

        shuffleButton = makeButton(colorScheme.imageSet().shuffleButton(), Layout.shuffleButtonSize);
        shuffleButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> didPressShuffle(delegate)));
        setButtonImage(shuffleButton, imageSet.shuffleButton(), false);
        
        alphabeticButton = makeButton(colorScheme.imageSet().alphabeticSortButton(), Layout.alphabeticButtonSize);
        alphabeticButton.addActionListener(e -> unwrappedPerform(delegate, delegate -> didPressAlphabeticSort(delegate)));
        setButtonImage(alphabeticButton, imageSet.alphabeticSortButton(), false);
    }
    
    private JToggleButton makeButton(ControlImageSet imageSet, Dimension size) {
        //Make the button
        JToggleButton button = new JToggleButton();
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        
        button.setIcon(new ImageIcon(imageSet.defaultImage().getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
        button.setPressedIcon(new ImageIcon(imageSet.pressedImage().getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
        button.setDisabledIcon(new ImageIcon(imageSet.disabledImage().getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
        
        return button;
    }
    
    // MARK: - Public functions
    
    public void enablePlayButton() {
        playButton.setVisible(true);
        pauseButton.setVisible(false);
        invalidate();
        repaint();
    }
    
    public void enablePauseButton() {
        playButton.setVisible(false);
        pauseButton.setVisible(true);
        invalidate();
        repaint();
    }
    
    // MARK: - Private functions
    
    private void didPressPlay(MusicControlPaneDelegate delegate) {
        if(delegate.didPressPlayButton(this)) {
            enablePauseButton();
        } else {
            enablePlayButton();
        }
    }
    
    private void didPressShuffle(MusicControlPaneDelegate delegate) {
        if(delegate.didPressShuffleButton(this)) {
            setButtonImage(shuffleButton, imageSet.shuffleButton(), true);
            setButtonImage(alphabeticButton, imageSet.alphabeticSortButton(), false);
        } else {
            setButtonImage(shuffleButton, imageSet.shuffleButton(), false);
        }
    }
    
    private void didPressAlphabeticSort(MusicControlPaneDelegate delegate) {
        if(delegate.didPressAlphabeticSortButton(this)) {
            setButtonImage(alphabeticButton, imageSet.alphabeticSortButton(),true);
            setButtonImage(shuffleButton, imageSet.shuffleButton(), false);
        } else {
            setButtonImage(alphabeticButton, imageSet.alphabeticSortButton(), false);
        }
    }
    
    private void setButtonImage(JToggleButton button, ControlImageSet imageSet, boolean isEnabled) {
        BufferedImage image = isEnabled ? imageSet.defaultImage() : imageSet.disabledImage();
        button.setIcon(new ImageIcon(image.getScaledInstance(button.getPreferredSize().width, button.getPreferredSize().height, Image.SCALE_SMOOTH)));
    }

    
}
