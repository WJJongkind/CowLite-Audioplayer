/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

/**
 * This class defines all the images that are needed in the application. We load them
 * straight into memory, as the user will see them on the main screen anyways. No need
 * to do some sort of lazy or delayed loading. YAGNI.
 * @author Wessel Jongkind
 */
public class UIImageSet extends ColorSchemeItem<UIImageSet> {
    
    // MARK: - Private properties
    
    private ControlImageSet playButtomImageSet;
    private ControlImageSet stopButtonImageSet;
    private ControlImageSet pauseButtonImageSet;
    private ControlImageSet shuffleButtonImageSet;
    private ControlImageSet alphabeticSortButtonImageSet;
    private ControlImageSet clearButtonImageSet;
    private ControlImageSet nextButtonImageSet;
    private ControlImageSet previousButtonImageSet;
    private ControlImageSet minimizeScreenButtonImageSet;
    private ControlImageSet stretchScreenButtonImageSet;
    private ControlImageSet closeScreenButtonImageSet;

    // MARK: - Initialisers
    
    /**
     * @param playButtomImageSet Image set for the play button.
     * @param stopButtonImageSet Image set for the stop button.
     * @param pauseButtonImageSet Image set for the pause button.
     * @param shuffleButtonImageSet Image set for the shuffle button.
     * @param alphabeticSortButtonImageSet Image set for the alphabetic sort button.
     * @param clearButtonImageSet Image set for the clear button.
     * @param nextButtonImageSet Image set for the next button.
     * @param previousButtonImageSet Image set for the previous button.
     * @param minimizeScreenButtonImageSet Image set for the minimize screen button.
     * @param stretchScreenButtonImageSet Image set for the stretch screen button.
     * @param closeScreenButtonImageSet  Image set for the close screen button.
     */
    public UIImageSet(
            ControlImageSet playButtomImageSet, 
            ControlImageSet stopButtonImageSet, 
            ControlImageSet pauseButtonImageSet, 
            ControlImageSet shuffleButtonImageSet, 
            ControlImageSet alphabeticSortButtonImageSet, 
            ControlImageSet clearButtonImageSet, 
            ControlImageSet nextButtonImageSet, 
            ControlImageSet previousButtonImageSet, 
            ControlImageSet minimizeScreenButtonImageSet, 
            ControlImageSet stretchScreenButtonImageSet, 
            ControlImageSet closeScreenButtonImageSet
    ) {
        this.playButtomImageSet = playButtomImageSet;
        this.stopButtonImageSet = stopButtonImageSet;
        this.pauseButtonImageSet = pauseButtonImageSet;
        this.shuffleButtonImageSet = shuffleButtonImageSet;
        this.alphabeticSortButtonImageSet = alphabeticSortButtonImageSet;
        this.clearButtonImageSet = clearButtonImageSet;
        this.nextButtonImageSet = nextButtonImageSet;
        this.previousButtonImageSet = previousButtonImageSet;
        this.minimizeScreenButtonImageSet = minimizeScreenButtonImageSet;
        this.stretchScreenButtonImageSet = stretchScreenButtonImageSet;
        this.closeScreenButtonImageSet = closeScreenButtonImageSet;
    }

    /**
     * @return The play button image set.
     */
    public ControlImageSet getPlayButtonImageSet() {
        return playButtomImageSet;
    }

    /**
     * @param playButtomImageSet The new image set.
     */
    public void setPlayButtomImageSet(ControlImageSet playButtomImageSet) {
        this.playButtomImageSet = playButtomImageSet;
        notifyObservers();
    }

    /**
     * @return The stop button image set.
     */
    public ControlImageSet getStopButtonImageSet() {
        return stopButtonImageSet;
    }

    /**
     * @param stopButtonImageSet The new image set.
     */
    public void setStopButtonImageSet(ControlImageSet stopButtonImageSet) {
        this.stopButtonImageSet = stopButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The pause button image set.
     */
    public ControlImageSet getPauseButtonImageSet() {
        return pauseButtonImageSet;
    }

    /**
     * @param pauseButtonImageSet The new image set.
     */
    public void setPauseButtonImageSet(ControlImageSet pauseButtonImageSet) {
        this.pauseButtonImageSet = pauseButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The shuffle button image set.
     */
    public ControlImageSet getShuffleButtonImageSet() {
        return shuffleButtonImageSet;
    }

    /**
     * @param shuffleButtonImageSet The new image set.
     */
    public void setShuffleButtonImageSet(ControlImageSet shuffleButtonImageSet) {
        this.shuffleButtonImageSet = shuffleButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The alphabetic sort button iamge set.
     */
    public ControlImageSet getAlphabeticSortButtonImageSet() {
        return alphabeticSortButtonImageSet;
    }

    /**
     * @param alphabeticSortButtonImageSet The new image set.
     */
    public void setAlphabeticSortButtonImageSet(ControlImageSet alphabeticSortButtonImageSet) {
        this.alphabeticSortButtonImageSet = alphabeticSortButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The clear button image set.
     */
    public ControlImageSet getClearButtonImageSet() {
        return clearButtonImageSet;
    }

    /**
     * @param clearButtonImageSet The new image set.
     */
    public void setClearButtonImageSet(ControlImageSet clearButtonImageSet) {
        this.clearButtonImageSet = clearButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The next button image set.
     */
    public ControlImageSet getNextButtonImageSet() {
        return nextButtonImageSet;
    }

    /**
     * @param nextButtonImageSet The new image set.
     */
    public void setNextButtonImageSet(ControlImageSet nextButtonImageSet) {
        this.nextButtonImageSet = nextButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The previous button image set.
     */
    public ControlImageSet getPreviousButtonImageSet() {
        return previousButtonImageSet;
    }

    /**
     * @param previousButtonImageSet The new image set.
     */
    public void setPreviousButtonImageSet(ControlImageSet previousButtonImageSet) {
        this.previousButtonImageSet = previousButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The minimize screen button image set.
     */
    public ControlImageSet getMinimizeScreenButtonImageSet() {
        return minimizeScreenButtonImageSet;
    }

    /**
     * @param minimizeScreenButtonImageSet The new image set.
     */
    public void setMinimizeScreenButtonImageSet(ControlImageSet minimizeScreenButtonImageSet) {
        this.minimizeScreenButtonImageSet = minimizeScreenButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The stretch screen button image set.
     */
    public ControlImageSet getStretchScreenButtonImageSet() {
        return stretchScreenButtonImageSet;
    }

    /**
     * @param stretchScreenButtonImageSet The new image set.
     */
    public void setStretchScreenButtonImageSet(ControlImageSet stretchScreenButtonImageSet) {
        this.stretchScreenButtonImageSet = stretchScreenButtonImageSet;
        notifyObservers();
    }

    /**
     * @return The close screen button image set.
     */
    public ControlImageSet getCloseScreenButtonImageSet() {
        return closeScreenButtonImageSet;
    }

    /**
     * @param closeScreenButtonImageSet The new image set.
     */
    public void setCloseScreenButtonImageSet(ControlImageSet closeScreenButtonImageSet) {
        this.closeScreenButtonImageSet = closeScreenButtonImageSet;
        notifyObservers();
    }
    
    // MARK: - ColorSchemeItem

    @Override
    protected UIImageSet copy() {
        return new UIImageSet(
                playButtomImageSet, 
                stopButtonImageSet, 
                pauseButtonImageSet, 
                shuffleButtonImageSet, 
                alphabeticSortButtonImageSet, 
                clearButtonImageSet, 
                nextButtonImageSet, 
                previousButtonImageSet, 
                minimizeScreenButtonImageSet, 
                stretchScreenButtonImageSet, 
                closeScreenButtonImageSet
        );
    }
    
}
