/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

/**
 *
 * @author Wessel
 */
public class UIImageSet {
    
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

    public ControlImageSet getPlayButtonImageSet() {
        return playButtomImageSet;
    }

    public void setPlayButtomImageSet(ControlImageSet playButtomImageSet) {
        this.playButtomImageSet = playButtomImageSet;
    }

    public ControlImageSet getStopButtonImageSet() {
        return stopButtonImageSet;
    }

    public void setStopButtonImageSet(ControlImageSet stopButtonImageSet) {
        this.stopButtonImageSet = stopButtonImageSet;
    }

    public ControlImageSet getPauseButtonImageSet() {
        return pauseButtonImageSet;
    }

    public void setPauseButtonImageSet(ControlImageSet pauseButtonImageSet) {
        this.pauseButtonImageSet = pauseButtonImageSet;
    }

    public ControlImageSet getShuffleButtonImageSet() {
        return shuffleButtonImageSet;
    }

    public void setShuffleButtonImageSet(ControlImageSet shuffleButtonImageSet) {
        this.shuffleButtonImageSet = shuffleButtonImageSet;
    }

    public ControlImageSet getAlphabeticSortButtonImageSet() {
        return alphabeticSortButtonImageSet;
    }

    public void setAlphabeticSortButtonImageSet(ControlImageSet alphabeticSortButtonImageSet) {
        this.alphabeticSortButtonImageSet = alphabeticSortButtonImageSet;
    }

    public ControlImageSet getClearButtonImageSet() {
        return clearButtonImageSet;
    }

    public void setClearButtonImageSet(ControlImageSet clearButtonImageSet) {
        this.clearButtonImageSet = clearButtonImageSet;
    }

    public ControlImageSet getNextButtonImageSet() {
        return nextButtonImageSet;
    }

    public void setNextButtonImageSet(ControlImageSet nextButtonImageSet) {
        this.nextButtonImageSet = nextButtonImageSet;
    }

    public ControlImageSet getPreviousButtonImageSet() {
        return previousButtonImageSet;
    }

    public void setPreviousButtonImageSet(ControlImageSet previousButtonImageSet) {
        this.previousButtonImageSet = previousButtonImageSet;
    }

    public ControlImageSet getMinimizeScreenButtonImageSet() {
        return minimizeScreenButtonImageSet;
    }

    public void setMinimizeScreenButtonImageSet(ControlImageSet minimizeScreenButtonImageSet) {
        this.minimizeScreenButtonImageSet = minimizeScreenButtonImageSet;
    }

    public ControlImageSet getStretchScreenButtonImageSet() {
        return stretchScreenButtonImageSet;
    }

    public void setStretchScreenButtonImageSet(ControlImageSet stretchScreenButtonImageSet) {
        this.stretchScreenButtonImageSet = stretchScreenButtonImageSet;
    }

    public ControlImageSet getCloseScreenButtonImageSet() {
        return closeScreenButtonImageSet;
    }

    public void setCloseScreenButtonImageSet(ControlImageSet closeScreenButtonImageSet) {
        this.closeScreenButtonImageSet = closeScreenButtonImageSet;
    }
    
}
