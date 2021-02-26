/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme.darkmode;

import cap.gui.colorscheme.BasicColorScheme;
import cap.gui.colorscheme.ButtonColorScheme;
import cap.gui.colorscheme.MenuColorScheme;
import cap.gui.colorscheme.TableColorScheme;
import cap.gui.colorscheme.SavedListsPaneColorScheme;
import cap.gui.colorscheme.SliderColorScheme;
import java.awt.Color;
import java.io.IOException;
import cap.gui.colorscheme.UIImageSet;
import cap.gui.colorscheme.ControlImageSet;
import cap.gui.colorscheme.DynamicFont;
import cap.gui.colorscheme.GeneralColorScheme;
import cap.gui.colorscheme.InputFieldColorScheme;
import cap.gui.colorscheme.OverlayColorScheme;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class defines the dark mode colorscheme for the application.
 * @author Wessel Jongkind
 */
public class DarkMode extends BasicColorScheme {
    
    // MARK: - Caching of ImageSet... Quite important
    
    private static UIImageSet imageSet;
    
    // MARK: - Constants
    
    private static final DynamicFont font = new DynamicFont(new Font("Dialog", Font.PLAIN, 11));
    private static final GeneralColorScheme generalColorScheme = new GeneralColorScheme(new Color(0x333333), new Color(0x909090));
    private static final MenuColorScheme menuColorScheme = new MenuColorScheme(new Color(0x909090), new Color(0x333333), new Color(0x909090));
    private static final TableColorScheme playlistPaneColorScheme = new TableColorScheme(new Color(0x909090), new Color(0xA2A2A2), new Color(0x333333), new Color(0xb00012), Color.white);
    private static final SavedListsPaneColorScheme savedListsColorScheme = new SavedListsPaneColorScheme(new Color(0x909090), new Color(0x333333), new Color(0xb00012), Color.white);
    private static final SliderColorScheme timeSliderColorScheme = new SliderColorScheme(new Color(0x909090), new Color(0xb00012));
    private static final SliderColorScheme volumeSliderColorScheme = new SliderColorScheme(new Color(0x909090), new Color(0xb00012));
    private static final ButtonColorScheme buttonColorScheme = new ButtonColorScheme(new Color(0x707070), new Color(0x909090), new Color(0x333333));
    private static final OverlayColorScheme overlayColorScheme = new OverlayColorScheme(new Color(0, 0, 0, 0.3f), Color.white);
    private static final InputFieldColorScheme inputFieldColorScheme = new InputFieldColorScheme(new Color(0x333333), new Color(0x909090), new Color(0x909090));
    
    // MARK: - Initialisers
    
    /**
     * 
     * @param resourcesFolder The folder where all resources for the app can be found.
     * @throws IOException When one or more of the required resources could not be loaded.
     */
    public DarkMode(File resourcesFolder) throws IOException {
        super(font, generalColorScheme, buttonColorScheme, inputFieldColorScheme, menuColorScheme, playlistPaneColorScheme, savedListsColorScheme, timeSliderColorScheme, volumeSliderColorScheme, overlayColorScheme, makeImageSet(resourcesFolder));
    }
    
    // MARK: - Private methods
    
    private static UIImageSet makeImageSet(File resourcesFolder) throws IOException {
        if(imageSet != null) {
            return imageSet;
        }
        String basePath = resourcesFolder.getAbsolutePath() + File.separatorChar + "graphics" + File.separatorChar;

        BufferedImage playDefault = ImageIO.read(new File(basePath + "play.png"));
        BufferedImage playPressed = ImageIO.read(new File(basePath + "playpressed.png"));
        BufferedImage playDisabled = ImageIO.read(new File(basePath + "playdisabled.png"));
        ControlImageSet playButton = new ControlImageSet(playDefault, playPressed, playDisabled);

        BufferedImage stopDefault = ImageIO.read(new File(basePath + "stop.png"));
        BufferedImage stopPressed = ImageIO.read(new File(basePath + "stoppressed.png"));
        BufferedImage stopDisabled = ImageIO.read(new File(basePath + "stopdisabled.png"));
        ControlImageSet stopButton = new ControlImageSet(stopDefault, stopPressed, stopDisabled);

        BufferedImage pauseDefault = ImageIO.read(new File(basePath + "pause.png"));
        BufferedImage pausePressed = ImageIO.read(new File(basePath + "pausepressed.png"));
        BufferedImage pauseDisabled = ImageIO.read(new File(basePath + "pausedisabled.png"));
        ControlImageSet pauseButton = new ControlImageSet(pauseDefault, pausePressed, pauseDisabled);

        BufferedImage shuffleDefault = ImageIO.read(new File(basePath + "shuffle.png"));
        BufferedImage shufflePressed = ImageIO.read(new File(basePath + "shufflepressed.png"));
        BufferedImage shuffleDisabled = ImageIO.read(new File(basePath + "shuffledisabled.png"));
        ControlImageSet shuffleButton = new ControlImageSet(shuffleDefault, shufflePressed, shuffleDisabled);

        BufferedImage alphabeticSortDefault = ImageIO.read(new File(basePath + "alphabeticsort.png"));
        BufferedImage alphabeticSortPressed = ImageIO.read(new File(basePath + "alphabeticsortpressed.png"));
        BufferedImage alphabeticSortDisabled = ImageIO.read(new File(basePath + "alphabeticsortdisabled.png"));
        ControlImageSet alphabeticSortButton = new ControlImageSet(alphabeticSortDefault, alphabeticSortPressed, alphabeticSortDisabled);

        BufferedImage clearDefault = ImageIO.read(new File(basePath + "clear.png"));
        BufferedImage clearPressed = ImageIO.read(new File(basePath + "clearpressed.png"));
        BufferedImage clearDisabled = ImageIO.read(new File(basePath + "cleardisabled.png"));
        ControlImageSet clearButton = new ControlImageSet(clearDefault, clearPressed, clearDisabled);

        BufferedImage nextDefault = ImageIO.read(new File(basePath + "next.png"));
        BufferedImage nextPressed = ImageIO.read(new File(basePath + "nextpressed.png"));
        BufferedImage nextDisabled = ImageIO.read(new File(basePath + "nextdisabled.png"));
        ControlImageSet nextButton = new ControlImageSet(nextDefault, nextPressed, nextDisabled);

        BufferedImage previousDefault = ImageIO.read(new File(basePath + "previous.png"));
        BufferedImage previousPressed = ImageIO.read(new File(basePath + "previouspressed.png"));
        BufferedImage previousDisabled = ImageIO.read(new File(basePath + "previousdisabled.png"));
        ControlImageSet previousButton = new ControlImageSet(previousDefault, previousPressed, previousDisabled);

        BufferedImage minimizeDefault = ImageIO.read(new File(basePath + "minimize.png"));
        BufferedImage minimizePressed = ImageIO.read(new File(basePath + "minimizepressed.png"));
        BufferedImage minimizeDisabled = ImageIO.read(new File(basePath + "minimizedisabled.png"));
        ControlImageSet minimizeScreenButton = new ControlImageSet(minimizeDefault, minimizePressed, minimizeDisabled);

        BufferedImage stretchDefault = ImageIO.read(new File(basePath + "stretch.png"));
        BufferedImage stretchPressed = ImageIO.read(new File(basePath + "stretchpressed.png"));
        BufferedImage stretchDisabled = ImageIO.read(new File(basePath + "stretchdisabled.png"));
        ControlImageSet stretchScreenButton = new ControlImageSet(stretchDefault, stretchPressed, stretchDisabled);

        BufferedImage closeDefault = ImageIO.read(new File(basePath + "close.png"));
        BufferedImage closePressed = ImageIO.read(new File(basePath + "closepressed.png"));
        BufferedImage closeDisabled = ImageIO.read(new File(basePath + "closedisabled.png"));
        ControlImageSet closeScreenButton = new ControlImageSet(closeDefault, closePressed, closeDisabled);
        
        return new UIImageSet(playButton, stopButton, pauseButton, shuffleButton, alphabeticSortButton, clearButton, nextButton, previousButton, minimizeScreenButton, stretchScreenButton, closeScreenButton);
    }
    
}
