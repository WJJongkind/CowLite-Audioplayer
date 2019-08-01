/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme.darkmode;

import cap.gui.colorscheme.ControlImageSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import cap.gui.colorscheme.UIImageSet;

/**
 *
 * @author Wessel
 */
class DarkModeImageSet implements UIImageSet {
    private final ControlImageSet playButton, 
                                 stopButton, 
                                 pauseButton, 
                                 shuffleButton, 
                                 alphabeticSortButton, 
                                 clearButton, 
                                 nextButton,
                                 previousButton,
                                 minimizeScreenButton,
                                 stretchScreenButton,
                                 closeScreenButton;


    public DarkModeImageSet() throws IOException {
        String basePath = "resources" + File.separatorChar + "graphics" + File.separatorChar; // TODO make configurable somewhere

        BufferedImage playDefault = ImageIO.read(new File(basePath + "play.png"));
        BufferedImage playPressed = ImageIO.read(new File(basePath + "playpressed.png"));
        BufferedImage playDisabled = ImageIO.read(new File(basePath + "playdisabled.png"));
        playButton = new ControlImageSet(playDefault, playPressed, playDisabled);

        BufferedImage stopDefault = ImageIO.read(new File(basePath + "stop.png"));
        BufferedImage stopPressed = ImageIO.read(new File(basePath + "stoppressed.png"));
        BufferedImage stopDisabled = ImageIO.read(new File(basePath + "stopdisabled.png"));
        stopButton = new ControlImageSet(stopDefault, stopPressed, stopDisabled);

        BufferedImage pauseDefault = ImageIO.read(new File(basePath + "pause.png"));
        BufferedImage pausePressed = ImageIO.read(new File(basePath + "pausepressed.png"));
        BufferedImage pauseDisabled = ImageIO.read(new File(basePath + "pausedisabled.png"));
        pauseButton = new ControlImageSet(pauseDefault, pausePressed, pauseDisabled);

        BufferedImage shuffleDefault = ImageIO.read(new File(basePath + "shuffle.png"));
        BufferedImage shufflePressed = ImageIO.read(new File(basePath + "shufflepressed.png"));
        BufferedImage shuffleDisabled = ImageIO.read(new File(basePath + "shuffledisabled.png"));
        shuffleButton = new ControlImageSet(shuffleDefault, shufflePressed, shuffleDisabled);

        BufferedImage alphabeticSortDefault = ImageIO.read(new File(basePath + "alphabeticsort.png"));
        BufferedImage alphabeticSortPressed = ImageIO.read(new File(basePath + "alphabeticsortpressed.png"));
        BufferedImage alphabeticSortDisabled = ImageIO.read(new File(basePath + "alphabeticsortdisabled.png"));
        alphabeticSortButton = new ControlImageSet(alphabeticSortDefault, alphabeticSortPressed, alphabeticSortDisabled);

        BufferedImage clearDefault = ImageIO.read(new File(basePath + "clear.png"));
        BufferedImage clearPressed = ImageIO.read(new File(basePath + "clearpressed.png"));
        BufferedImage clearDisabled = ImageIO.read(new File(basePath + "cleardisabled.png"));
        clearButton = new ControlImageSet(clearDefault, clearPressed, clearDisabled);

        BufferedImage nextDefault = ImageIO.read(new File(basePath + "next.png"));
        BufferedImage nextPressed = ImageIO.read(new File(basePath + "nextpressed.png"));
        BufferedImage nextDisabled = ImageIO.read(new File(basePath + "nextdisabled.png"));
        nextButton = new ControlImageSet(nextDefault, nextPressed, nextDisabled);

        BufferedImage previousDefault = ImageIO.read(new File(basePath + "previous.png"));
        BufferedImage previousPressed = ImageIO.read(new File(basePath + "previouspressed.png"));
        BufferedImage previousDisabled = ImageIO.read(new File(basePath + "previousdisabled.png"));
        previousButton = new ControlImageSet(previousDefault, previousPressed, previousDisabled);

        BufferedImage minimizeDefault = ImageIO.read(new File(basePath + "minimize.png"));
        BufferedImage minimizePressed = ImageIO.read(new File(basePath + "minimizepressed.png"));
        BufferedImage minimizeDisabled = ImageIO.read(new File(basePath + "minimizedisabled.png"));
        minimizeScreenButton = new ControlImageSet(minimizeDefault, minimizePressed, minimizeDisabled);

        BufferedImage stretchDefault = ImageIO.read(new File(basePath + "stretch.png"));
        BufferedImage stretchPressed = ImageIO.read(new File(basePath + "stretchpressed.png"));
        BufferedImage stretchDisabled = ImageIO.read(new File(basePath + "stretchdisabled.png"));
        stretchScreenButton = new ControlImageSet(stretchDefault, stretchPressed, stretchDisabled);

        BufferedImage closeDefault = ImageIO.read(new File(basePath + "close.png"));
        BufferedImage closePressed = ImageIO.read(new File(basePath + "closepressed.png"));
        BufferedImage closeDisabled = ImageIO.read(new File(basePath + "closedisabled.png"));
        closeScreenButton = new ControlImageSet(closeDefault, closePressed, closeDisabled);
    }

    @Override
    public ControlImageSet playButton() {
        return playButton;
    }

    @Override
    public ControlImageSet stopButton() {
        return stopButton;
    }

    @Override
    public ControlImageSet pauseButton() {
        return pauseButton;
    }

    @Override
    public ControlImageSet shuffleButton() {
        return shuffleButton;
    }

    @Override
    public ControlImageSet alphabeticSortButton() {
        return alphabeticSortButton;
    }

    @Override
    public ControlImageSet clearButton() {
        return clearButton;
    }

    @Override
    public ControlImageSet nextButton() {
        return nextButton;
    }

    @Override
    public ControlImageSet previousButton() {
        return previousButton;
    }

    @Override
    public ControlImageSet minimizeScreenButton() {
        return minimizeScreenButton;
    }

    @Override
    public ControlImageSet stretchScreenButton() {
        return stretchScreenButton;
    }

    @Override
    public ControlImageSet closeScreenButton() {
        return closeScreenButton;
    }
}
