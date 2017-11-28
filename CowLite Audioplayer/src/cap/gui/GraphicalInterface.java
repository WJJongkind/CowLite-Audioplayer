package cap.gui;

import cap.core.ApplicationController;
import cap.core.PropertiesManager;
import cap.core.audio.AudioController;
import cap.core.audio.FileAudioPlayer;
import cap.gui.overlay.InfoComponent;
import cap.gui.overlay.TranslucentFrame;
import cap.gui.settings.SettingsMenu;
import cap.util.IO;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import sun.java2d.SunGraphicsEnvironment;

/**
 * (c) Copyright
 * The main GUI. Here all buttons, labels, frame-items etc. are created
 * & added to the interface of CowLite Audio Player.
 */
public class GraphicalInterface extends JFrame
{   
    private DefaultListModel savedmodel = new DefaultListModel();
    private JScrollPane playlist, savedLists;
    private JList songlist, artistlist, albumlist, savedListText;
    private JButton maximizeButton, exitButton, playButton, stopButton,
            prevButton, nextButton, clearButton, shuffleButton, alphabeticButton;
    private JSlider volumeSlider, timeSlider;
    private TimeSlider timeUI;
    private VolumeSlider volumeUI;
    private JSplitPane splitpane, songs, artists;
    private JFileChooser filechooser;
    private JMenuBar menufile;
    private JMenu filemenu, helpmenu, settingsmenu;
    private JMenuItem help, about, setHotkeys, saveList, removeList, importYoutube,
            setGraphics, setOverlay;
    private JPanel top, left, right, bottom;
    private boolean maximized = false;
    private Dimension oldDimension;
    private Point oldPoint;
    private GridBagConstraints c;
    
    private Color BACKGROUND = new Color(0x333333);
    private Color PLAYLISTTEXT = new Color(0x333333);
    private Color LISTBG = new Color(0x8E9191);
    private Color MENUTEXTCOLOR = new Color(0x8E9191);
    private final int SQUAREBUTTON = 20;
    private final int RECTBUTTON = 15;
    
    private final TranslucentFrame tf;
    
    private final Map<String, Object> GRAPHICS;
    private final AudioController AUDIO;
    private final PropertiesManager PROPERTIES;
    private ApplicationController CONTROLLER;
    
    /**
     * Adds all the components to the interface (this class)
     * @param title title of the JFrame
     */
    public GraphicalInterface(String title, Map<String, Object> graphics, AudioController audio, PropertiesManager properties, ApplicationController ctrl) throws Exception
    {
        super(title);
        
        GRAPHICS = graphics;
        AUDIO = audio;
        PROPERTIES = properties;
        CONTROLLER = ctrl;
        
        //Setting the correct color values (or uses defaults if not specified in saved file)
        try{
            BACKGROUND = (Color) GRAPHICS.get("background");
            LISTBG = (Color) GRAPHICS.get("listbg");
            PLAYLISTTEXT = (Color) GRAPHICS.get("listtext");
            MENUTEXTCOLOR = (Color) GRAPHICS.get("menutext");
        }catch(Exception e){}
        
        //For maximization
        GraphicsConfiguration config = getGraphicsConfiguration();
        Rectangle usableBounds = SunGraphicsEnvironment.getUsableBounds(config.getDevice());
        setMaximizedBounds(usableBounds);
        
        UIManager.put("SplitPane.background", BACKGROUND);
        this.setUndecorated(true);
        
        //For resizing the interface
        WindowMouseListener wndw = new WindowMouseListener(this);
        getContentPane().addMouseListener(wndw);
        getContentPane().addMouseMotionListener(wndw);
        
        //Creation of all components
        volumeSlider = new JSlider();
        setLayout(new GridBagLayout());
        volumeSlider.setOrientation(JSlider.VERTICAL);
        volumeSlider.setValue(Integer.parseInt(PROPERTIES.getAudioProperties().get("volume")));
        volumeSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                PROPERTIES.getAudioProperties().replace("volume", volumeSlider.getValue() + "");
                AUDIO.getPlayer().setVolume(volumeSlider.getValue());
            }
        });
        volumeSlider.setBackground(BACKGROUND);
        volumeUI = new VolumeSlider(GRAPHICS){
            @Override
            protected void scrollDueToClickInTrack(int direction)
            {
                int value;
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    slider.setValue(this.valueForXPosition(slider.getMousePosition().x));
                } else if (slider.getOrientation() == JSlider.VERTICAL) {
                    slider.setValue(this.valueForYPosition(slider.getMousePosition().y));
                }
            }
        };
        volumeSlider.setUI(volumeUI);
        
        timeSlider = new JSlider();
        timeUI = new TimeSlider(GRAPHICS, AUDIO){
            @Override
            protected void scrollDueToClickInTrack(int direction)
            {
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    slider.setValue(this.valueForXPosition(slider.getMousePosition().x));
                } else if (slider.getOrientation() == JSlider.VERTICAL) {
                    slider.setValue(this.valueForYPosition(slider.getMousePosition().y));
                }
            }
        };
        timeSlider.setUI(timeUI);
        timeSlider.setBackground(BACKGROUND);
        setLayout(new GridBagLayout());
        timeSlider.setOrientation(SwingConstants.HORIZONTAL);
        timeSlider.setValue(0);
        timeSlider.setMinimum(0); 
        timeSlider.addChangeListener(new ChangeListener(){
            int lastIndex = -1;
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = AUDIO.getPlayer().getIndex();
                int sliderVal = timeSlider.getValue();
                int position = (int) AUDIO.getPlayer().getPosition();
                if((sliderVal < position - 1 || sliderVal > position + 1) && index == lastIndex)
                    AUDIO.getPlayer().seek(timeSlider.getValue());
                lastIndex = index;
            }
        });
        
        filechooser = new JFileChooser();
        filechooser.setCurrentDirectory(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\playlists\\"));
        
        makeMenu();
        
        playButton = new JButton();
        makePlayButton(playButton, SQUAREBUTTON, SQUAREBUTTON);
        playButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(AUDIO.getPlayer() != null && AUDIO.getPlayer().isPaused())
                    CONTROLLER.playEvent();
                else if(AUDIO.getPlayer() != null)
                    CONTROLLER.pauseEvent();
                else
                    CONTROLLER.playEvent();
            }
        });
        
        stopButton = new JButton();
        makeButton(stopButton, "stop", SQUAREBUTTON, SQUAREBUTTON);
        stopButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                CONTROLLER.stopEvent();
            }
        });
        
        nextButton = new JButton();
        makeButton(nextButton, "next", SQUAREBUTTON, SQUAREBUTTON);
        nextButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                CONTROLLER.nextSongEvent();
            }
        });
        
        prevButton = new JButton();
        makeButton(prevButton, "prev", SQUAREBUTTON, SQUAREBUTTON);
        prevButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                CONTROLLER.previousSongEvent();
            }
        });
        
        clearButton = new JButton();
        makeButton(clearButton, "clear", RECTBUTTON * 2 + 8, RECTBUTTON + 4);
        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(AUDIO.getPlayer() != null)
                {
                    AUDIO.getPlayer().stop();
                    AUDIO.getPlayer().clearList();
                }
                
                savedListText.clearSelection();
                setPlayButton();
            } 
        });
        
        GraphicalInterface src = this;
        exitButton = new JButton();
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(src, WindowEvent.WINDOW_CLOSING));
            }
        });
        exitButton.setPreferredSize(new Dimension(35,30));
        exitButton.setMinimumSize(new Dimension(35, 30));
        exitButton.setMaximumSize(new Dimension(35, 30));
        exitButton.setFocusPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setOpaque(false);
        exitButton.setBackground(BACKGROUND);
        
        Image play = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + "exit" + ".png"));
        ImageIcon icon = new ImageIcon(play.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
        exitButton.setIcon(icon);
        play.flush();
            
        maximizeButton = new JButton();
        maximizeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!maximized)
                {
                    oldDimension = new Dimension(getWidth(), getHeight());
                    oldPoint = new Point(getLocation());
                    setExtendedState(MAXIMIZED_BOTH); 
                    maximized = true;
                }
                else
                {
                    maximized = false;
                    setSize(oldDimension);
                    setLocation(oldPoint);
                }
            }
        });
        maximizeButton.setPreferredSize(new Dimension(30,30));
        maximizeButton.setMinimumSize(new Dimension(30,30));
        maximizeButton.setMaximumSize(new Dimension(30,30));
        maximizeButton.setFocusPainted(false);
        maximizeButton.setContentAreaFilled(false);
        maximizeButton.setBorderPainted(false);
        maximizeButton.setOpaque(false);
        maximizeButton.setBackground(BACKGROUND);
        
        Image maximizeImage = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + "maximize" + ".png"));
        ImageIcon maximizeIcon = new ImageIcon(maximizeImage.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
        maximizeButton.setIcon(maximizeIcon);
        maximizeImage.flush();

        
        shuffleButton = new JButton();
        shuffleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                AUDIO.getPlayer().stop();
                AUDIO.getPlayer().shuffle();
                AUDIO.getPlayer().play();

                if(AUDIO.getPlayer().getShuffled()) 
                    setShuffleActive();
                else
                    setShuffleInactive();
            }
        });
        
        shuffleButton.setPreferredSize(new Dimension((int)(RECTBUTTON / 0.75), RECTBUTTON));
        shuffleButton.setMinimumSize(new Dimension((int)(RECTBUTTON / 0.75), RECTBUTTON));
        shuffleButton.setMaximumSize(new Dimension((int)(RECTBUTTON / 0.75), RECTBUTTON));
        shuffleButton.setFocusPainted(false);
        shuffleButton.setContentAreaFilled(false);
        shuffleButton.setBorderPainted(false);
        shuffleButton.setBackground(BACKGROUND);
        shuffleButton.setOpaque(false);
        setShuffleInactive();
        
        alphabeticButton = new JButton();
        alphabeticButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                AUDIO.getPlayer().stop();
                AUDIO.getPlayer().alphabetical();
                AUDIO.getPlayer().play();
                
                if(AUDIO.getPlayer().getAlphabetical())
                    setAlphabeticActive();
                else
                    setAlphabeticInactive();
            }
        });
        alphabeticButton.setPreferredSize(new Dimension((int)(RECTBUTTON / 0.65), RECTBUTTON));
        alphabeticButton.setMinimumSize(new Dimension((int)(RECTBUTTON / 0.65), RECTBUTTON));
        alphabeticButton.setMaximumSize(new Dimension((int)(RECTBUTTON / 0.65), RECTBUTTON));
        alphabeticButton.setFocusPainted(false);
        alphabeticButton.setContentAreaFilled(false);
        alphabeticButton.setBorderPainted(false);
        alphabeticButton.setBackground(BACKGROUND);
        alphabeticButton.setOpaque(false);
        setAlphabeticInactive();
        
        
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(25,25));
        button.setMinimumSize(new Dimension(25,25));
        button.setMaximumSize(new Dimension(25,25));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(BACKGROUND);
        
        JPanel button2 = new JPanel();
        button2.setPreferredSize(new Dimension(25,20));
        button2.setMinimumSize(new Dimension(25,20));
        button2.setMaximumSize(new Dimension(25,20));
        button2.setOpaque(false);
        button2.setBackground(BACKGROUND);
        
        Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
        for(Map.Entry<String, String> entry : AUDIO.getPlaylists().entrySet())
            savedmodel.addElement(entry.getKey());
        savedListText = new JList(savedmodel);
        savedListText.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(KeyEvent.VK_DELETE == e.getKeyCode() && savedListText.getSelectedIndex() > -1)
                {
                    try{
                        File f = new File(AUDIO.getPlaylistPath((String)savedListText.getSelectedValue()));
                        if(f.toString().contains("<Empty List>"))
                            return;
                        while(f.exists())
                            f.delete();

                        AUDIO.removePlaylist((String) savedListText.getSelectedValue());
                        
                        DefaultListModel model = new DefaultListModel();
                        ListModel oldList = savedListText.getModel();
                        for(int i = 0; i < oldList.getSize(); i++)
                            if(!oldList.getElementAt(i).equals(savedListText.getSelectedValue()))
                                model.addElement(oldList.getElementAt(i));
                        
                        savedListText.setModel(model);
                        
                        AUDIO.getPlayer().stop();
                        AUDIO.getPlayer().clearList();

                        PROPERTIES.storeProperties(properties.getPlaylists());
                        System.out.println("finished");
                        setPlayButton();
                    }catch(Exception ex){
                        System.out.println("STACK");
                        ex.printStackTrace();
                    }
                }
            }
        });
        savedLists = new JScrollPane(savedListText);
        savedLists.setBorder(null);
        savedLists.setViewportBorder(border);
        savedLists.setBorder(border);
        savedListText.setBackground(LISTBG);
        addSavedListListener();
        
        songlist = new JList();
        artistlist = new JList();
        albumlist = new JList();
        artists = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, artistlist, albumlist);
        artists.setUI(new BasicSplitPaneUI(){
           public BasicSplitPaneDivider createDefaultdivider()
           {
               return new BasicSplitPaneDivider(this)
               {
                   @Override
                   public void setBorder(Border b)
                   {
                       
                   }
               };
           }
        });
        artists.setDividerLocation(100);
        artists.setBorder(null);
        BasicSplitPaneDivider divider = (BasicSplitPaneDivider) artists.getComponent(2);
        divider.setBackground(BACKGROUND);
        divider.setBorder(null);
        artists.setDividerSize(3);
        artists.setMinimumSize(new Dimension(0,0));
        songs = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, songlist, artists);
        songs.setUI(new BasicSplitPaneUI(){
           public BasicSplitPaneDivider createDefaultdivider()
           {
               return new BasicSplitPaneDivider(this)
               {
                   @Override
                   public void setBorder(Border b)
                   {
                       
                   }
               };
           }
        });
        songs.setDividerLocation(100);
        songs.setBorder(null);
        divider = (BasicSplitPaneDivider) songs.getComponent(2);
        divider.setBackground(BACKGROUND);
        divider.setBorder(null);
        songs.setDividerSize(3);
        songs.setMinimumSize(new Dimension(0,0));
        artists.setMinimumSize(new Dimension(0,0));
        songlist.setMinimumSize(new Dimension(0,0));
        artistlist.setMinimumSize(new Dimension(0,0));
        albumlist.setMinimumSize(new Dimension(0,0));
        albumlist.setBackground(LISTBG);
        songlist.setBackground(LISTBG);
        artistlist.setBackground(LISTBG);
        playlist = new JScrollPane(songs);
        playlist.setBorder(null);
        playlist.setViewportBorder( border );
        playlist.setBorder( border );
        playlist.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        playlist.setMinimumSize(new Dimension(0,0));
        addListListener();
        
        playlist.getVerticalScrollBar().setUnitIncrement(12);
        splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, savedLists, playlist);
        splitpane.setUI(new BasicSplitPaneUI(){
           public BasicSplitPaneDivider createDefaultdivider()
           {
               return new BasicSplitPaneDivider(this)
               {
                   @Override
                   public void setBorder(Border b)
                   {
                       
                   }
               };
           }
        });
        splitpane.setDividerLocation(100);
        splitpane.setBorder(null);
        divider = (BasicSplitPaneDivider) splitpane.getComponent(2);
        divider.setBackground(BACKGROUND);
        divider.setBorder(null);
        splitpane.setDividerSize(6);
        splitpane.setMinimumSize(new Dimension(0,0));
        
        //Add components to the interface
        Container controller = getContentPane();
        controller.setBackground(BACKGROUND);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.RELATIVE;
        
        c = insertComponent(c, c.NONE, 2, 6, 0, 0, 1, 1);
        controller.add(button, c);
        c = insertComponent(c, c.BOTH, 3, 2, 0, 0, 7, 1);
        makeMenu();
        controller.add(menufile, c);
        c = insertComponent(c, GridBagConstraints.BOTH, 11,2,0,0,1,1);
        controller.add(exitButton,c);
        c = insertComponent(c, c.HORIZONTAL, 9, 2, 1, 0, 1, 1);
        controller.add(button2, c);
        c = insertComponent(c, c.NONE, 10, 2, 0, 0, 1, 1);
        controller.add(maximizeButton, c);
        c = insertComponent(c, GridBagConstraints.BOTH, 3, 3, 1, 1, 8, c.gridheight);
        controller.add(splitpane, c);
        c = insertComponent(c, GridBagConstraints.HORIZONTAL, 3, 4, 1, 0, 8, 1);
        controller.add(timeSlider, c);
        
        c = insertComponent(c, c.NONE, 3, 6, 0, 0, 1, 1);
        controller.add(prevButton, c);
        c = insertComponent(c, c.fill, 4, c.gridy, c.weightx, c.weighty, 1, c.gridheight);
        controller.add(playButton, c);
        c = insertComponent(c, c.fill, 5, c.gridy, c.weightx, c.weighty, c.gridwidth, c.gridheight);
        controller.add(stopButton, c);
        c = insertComponent(c, c.fill, 6, c.gridy, c.weightx, c.weighty, c.gridwidth, c.gridheight);
        controller.add(nextButton, c);
        c.anchor = GridBagConstraints.LINE_START;
        c = insertComponent(c, c.fill, 9, c.gridy, c.weightx, c.weighty, c.gridwidth, c.gridheight);
        controller.add(clearButton, c);
        c = insertComponent(c, c.fill, 7, c.gridy, c.weightx, c.weighty, c.gridwidth, c.gridheight);
        controller.add(shuffleButton, c);
        c = insertComponent(c, c.fill, 8, c.gridy, c.weightx, c.weighty, c.gridwidth, c.gridheight);
        controller.add(alphabeticButton, c);
        c = insertComponent(c, c.VERTICAL, 11, 3, 0, c.weighty, 1, 2);
        controller.add(volumeSlider, c);
        
        this.addWindowFocusListener(new WindowFocusListener(){
            @Override
            public void windowGainedFocus(WindowEvent e)
            {
                timeSlider.setVisible(true);
            }

            @Override
            public void windowLostFocus(WindowEvent e)
            {
                timeSlider.setVisible(false);
            }
        });
        
        tf = new TranslucentFrame();
        tf.setAlwaysOnTop(true);
        if(graphics.get("showOverlay").equals("true"))
            tf.setVisible(true);
        tf.getInfoComponent().setActiveSizes((String)graphics.get("overlaySize"));
        System.out.println("created!");
    }

    /**
     * return a gridbagconstraints object with given parameters
     * @param c
     * @param fill
     * @param gridx
     * @param gridy
     * @param weightx
     * @param weighty
     * @param gridwidth
     * @param gridheight
     * @return 
     */
    private GridBagConstraints insertComponent(GridBagConstraints c, int fill, int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight)
    {
       c.fill = fill;
       c.gridx = gridx;
       c.gridy = gridy;
       c.weightx = weightx;
       c.weighty = weighty;
       c.gridwidth = gridwidth;
       c.gridheight = gridheight;
       return c;
    }
    
    public void orderDividers()
    {
        Dimension size = splitpane.getSize();
        int width = (int)(size.getWidth() - splitpane.getDividerSize() - splitpane.getDividerLocation());
        songs.setSize(width, songs.getHeight());
        
        revalidate();
        
        System.out.println(width);
        
        songs.setDividerLocation((int)(width*(1.0/3.0 - 0.03)));
        revalidate();
        
        int location = (int)(width * 0.667) / 2;
        artists.setDividerLocation(location);
        revalidate();
    }
    
    private void makeButton(final JButton button, String type, int x, int y)
    {
        //Make the button
        button.setPreferredSize(new Dimension(x,y));
        button.setMinimumSize(new Dimension(x,y));
        button.setMaximumSize(new Dimension(x,y));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(BACKGROUND);
        
        //Adding the correct images to the button
        try{
            Image play = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + type + ".png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
            button.setIcon(icon);
            play.flush();
        }catch(Exception e){System.out.println(e + "makebutton");}
        
        //Change icon upon click events
        button.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                try{
                Image play = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + type + "pressed.png"));
                ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
                button.setIcon(icon);
                play.flush();
                }catch(Exception f){System.out.println(f + "mousepressed");}
            }
            
            public void mouseReleased(MouseEvent e)
            {
                try{
                Image play = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + type + ".png"));
                ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
                button.setIcon(icon);
                play.flush();
                }catch(Exception f){System.out.println(f + "mousereleased");}
            }
        });
    }
    
    private void makePlayButton(final JButton button, int x, int y)
    {
        //make the button
        button.setPreferredSize(new Dimension(x,y));
        button.setMinimumSize(new Dimension(x,y));
        button.setMaximumSize(new Dimension(x,y));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(BACKGROUND);
        
        setPlayButton();
        
        button.addMouseListener(new MouseAdapter(){
            //Sets the correct image for the playbutton (pause or play image)
            public void mousePressed(MouseEvent e)
            {
                if(AUDIO.getPlayer() == null || !AUDIO.getPlayer().isPaused())
                    setPlayButtonPressed();
                else if(AUDIO.getPlayer() != null)
                    setPauseButtonPressed();
            }
            public void mouseReleased(MouseEvent e)
            {
            }
        });
    }
    
    /**
     * Changes the song to the selected song on the interface
     */
    private void addListListener()
    {
        ListSelectionListener listener = new ListSelectionListener()
        {
            boolean locked = false;
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                
                if(!locked)
                {
                    locked = true;
                    int index;
                    
                    int songindex = songlist.getSelectedIndex();
                    int albumindex = albumlist.getSelectedIndex();
                    int artistindex = artistlist.getSelectedIndex();
                    
                    if(songindex == albumindex)
                        index = artistindex;
                    else if(songindex == artistindex)
                        index = albumindex;
                    else
                        index = songindex;
                    
                    songlist.setSelectedIndex(index);
                    albumlist.setSelectedIndex(index);
                    artistlist.setSelectedIndex(index);
                    
                    songindex = songlist.getSelectedIndex();
                    albumindex = albumlist.getSelectedIndex();
                    artistindex = artistlist.getSelectedIndex();
                    
                    if(songindex != albumindex)
                        if(songindex != artistindex)
                            songlist.setSelectedIndex(albumindex);
                        else if(albumindex != artistindex)
                            albumlist.setSelectedIndex(songindex);
                        else
                            artistlist.setSelectedIndex(songindex);
                            
                    
                    if(songlist.getSelectedIndex() > -1 && songlist.getSelectedIndex() != AUDIO.getPlayer().getIndex())
                        AUDIO.getPlayer().selectSong(songlist.getSelectedIndex());
                    locked = false;
                }
            }
        };
        albumlist.addListSelectionListener(listener);
        songlist.addListSelectionListener(listener);
        artistlist.addListSelectionListener(listener);
    }
    
    /**
     * Changes the playlist to the selected playlist on the interface
     */
    private void addSavedListListener()
    {
        savedListText.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if(savedListText.getSelectedIndex() != -1)
                    AUDIO.loadPlaylist((String) savedListText.getSelectedValue());
            }
        });
    }
    
    /**
     * Makes the menubar
     */
    private void makeMenu()
    {
        setGraphics = new JMenuItem("Change Layout");
        menufile = new JMenuBar();
        menufile.setBorder(null);
        filemenu = new JMenu("file");
        saveList = new JMenuItem("Save Playlist");
        removeList = new JMenuItem("Remove Selected Playlist");
        importYoutube = new JMenuItem("Import YouTube Playlist");
        filemenu.add(saveList);
        setGraphics.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new GraphicalSettingsMenu();
            }
        });
        saveList.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(AUDIO.getPlayer() instanceof FileAudioPlayer)
                    IO.saveFile(AUDIO);
            }
        });
        removeList.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                IO.removePlaylist(AUDIO, CONTROLLER, AUDIO.getPlaylistPath((String) savedListText.getSelectedValue()));
                savedListText.remove(savedListText.getSelectedIndex());
            }
        });
        importYoutube.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                IO.importYoutube(AUDIO);
            }
        });
        filemenu.add(removeList);
        filemenu.add(importYoutube);
        filemenu.setForeground(MENUTEXTCOLOR);
        menufile.add(filemenu);
        //add(menu);
        
        settingsmenu = new JMenu("settings");
        setHotkeys = new JMenuItem("Set Hotkeys");
        setHotkeys.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new SettingsMenu(PROPERTIES);
            }
        });
        
        setOverlay = new JMenuItem("Change Overlay");
        setOverlay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new OverlaySettings();
            }
        });
        settingsmenu.add(setHotkeys);
        settingsmenu.add(setGraphics);
        settingsmenu.add(setOverlay);
        settingsmenu.setForeground(MENUTEXTCOLOR);
        menufile.add(settingsmenu);
        
        helpmenu = new JMenu("help");
        help = new JMenuItem("settings & controls");
        about = new JMenuItem("about...");
        about.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Desktop.getDesktop().edit(new File(IO.getDocumentsFolder() + "/CowLite Audio Player/resources/infofiles/about.txt"));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        help.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Desktop.getDesktop().edit(new File(IO.getDocumentsFolder() + "/CowLite Audio Player/resources/infofiles/controls.txt"));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        helpmenu.add(help);
        helpmenu.add(about);
        menufile.add(helpmenu);
        helpmenu.setForeground(MENUTEXTCOLOR);
        menufile.setEnabled(false);
        menufile.setBackground(BACKGROUND);
    } 
    
    /**
     * for recolor events: color of the frame
     * @param c color
     */
    private void setNewBackground(Color c)
    {
        System.out.println(c);
        System.out.println(BACKGROUND);
        BACKGROUND = c;
        getContentPane().setBackground(c);
        volumeSlider.setBackground(c);
        maximizeButton.setBackground(c);
        volumeSlider.setBackground(c);
        timeSlider.setBackground(c);
        splitpane.setBackground(c);
        bottom.setBackground(c);
        left.setBackground(c);
        top.setBackground(c);
        right.setBackground(c);
        menufile.setBackground(c);
    }
    
    /**
     * for recolor events: color of the menutext
     * @param c color
     */
    private void setMenuTextColor(Color c)
    {
        MENUTEXTCOLOR = c;
        helpmenu.setForeground(c);
        menufile.setForeground(c);
        help.setForeground(c);
        about.setForeground(c);
        setHotkeys.setForeground(c);
        saveList.setForeground(c);
        removeList.setForeground(c);
        settingsmenu.setForeground(c);
        filemenu.setForeground(c);
        setHotkeys.setForeground(c);
        setGraphics.setForeground(c);
    }
    
    /**
     * for recolor events: playlist/savedlist background color
     * @param c color
     */
    private void setListBG(Color c)
    {
        LISTBG = c;
        
        songlist.setBackground(c);
        albumlist.setBackground(c);
        artistlist.setBackground(c);
        savedListText.setBackground(c);
        //playlist.setBackground(c);
        savedLists.setBackground(c);
    }
    
    /**
     * for recolor events: playlist/savedlist foreground color
     * @param c color
     */
    private void setListFG(Color c)
    {
        PLAYLISTTEXT = c;
        
        songlist.setForeground(c);
        artistlist.setForeground(c);
        albumlist.setForeground(c);
        savedListText.setForeground(c);
        //playlist.setForeground(c);
        savedLists.setForeground(c);
    }
    
    private void switchImage(JButton target, String imagePath, double width, double height)
    {
        try{
            Image img = ImageIO.read(new File(imagePath));
            ImageIcon icon = new ImageIcon(img.getScaledInstance(SQUAREBUTTON - (SQUAREBUTTON / 10), SQUAREBUTTON - (SQUAREBUTTON / 10),100));
            target.setIcon(icon);
            img.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * change the playbutton to have a play icon
     */
    public void setPlayButton()
    {
        try{
            switchImage(playButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\play.png", RECTBUTTON, RECTBUTTON);
        }catch(Exception e){}
    }
    
    public void setPauseButton()
    {
        try{
            switchImage(playButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\pause.png", RECTBUTTON, RECTBUTTON);
        }catch(Exception f){System.out.println(f);}
    }
    
    public void setPlayButtonPressed()
    {
        try{
            switchImage(playButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\playpressed.png", RECTBUTTON, RECTBUTTON);
        }catch(Exception e){}
    }
    
    public void setPauseButtonPressed()
    {
        try{
            switchImage(playButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\pausepressed.png", RECTBUTTON, RECTBUTTON);
        }catch(Exception f){System.out.println(f);}
    }
    
    /**
     * to show that the shufflefunction is on
     */
    public void setShuffleActive()
    {
        setAlphabeticInactive();
        try{
            switchImage(shuffleButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\shufflepressed.png", RECTBUTTON / 0.75, RECTBUTTON / 0.75);
        }catch(Exception f){System.out.println(f);}
    }
    
    /**
     * to show that the shufflefunction is off
     */
    public void setShuffleInactive()
    {
        try{
            switchImage(shuffleButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\shuffle.png", RECTBUTTON / 0.75, RECTBUTTON / 0.75);
        }catch(Exception f){System.out.println(f);}
    }    
    
    public void setAlphabeticActive()
    {
        setShuffleInactive();
        try{
            switchImage(alphabeticButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\AlphabeticEnabled.png", RECTBUTTON / 0.65, RECTBUTTON / 0.65);
        }catch(Exception f){System.out.println(f);}
    }
    
    public void setAlphabeticInactive()
    {
        try{
            switchImage(alphabeticButton, IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\AlphabeticDisabled.png", RECTBUTTON / 0.65, RECTBUTTON / 0.65);
        }catch(Exception f){System.out.println(f);}
    }
    
    public void setInitialSize(int width, int height)
    {
        setSize(width, height);
        oldDimension = getSize();
        oldPoint = getLocation();
    }
    
    public void setOldLocation(Point oldLocation)
    {
        oldPoint = oldLocation;
    }
    
    public void setOldSize(Dimension oldSize)
    {
        oldDimension = oldSize;
    }
    
    public Point getOldLocation()
    {
        return oldPoint;
    }
    
    public Dimension getOldSize()
    {
        return oldDimension;
    }    
        
    public void updateOverlay(String time, String song, double volume)
    {
        InfoComponent info = tf.getInfoComponent();
        info.setSong(song);
        System.out.println(time);
        info.setTime(time);
        info.setVolume(volume);
        tf.repaint();
    }

    public void repositionOverlay(int addX, int addY)
    {
        InfoComponent info = tf.getInfoComponent();

        info.changeOffsetX(addX);
        info.changeOffsetY(addY);
        info.repaint();
    }
    
    public void toggleOverlay()
    {
        tf.setVisible(!tf.isVisible());
    }

    public void setOverlaySettings(String size)
    {
        tf.getInfoComponent().setActiveSizes(size);
    }
    
    public void setTimeSliderPosition(int position, int max)
    {
        timeSlider.setMaximum(max);
        timeSlider.setValue(position);
    }
    
    public void setPlaylistModels(DefaultListModel songs, DefaultListModel artists, DefaultListModel albums)
    {
        songlist.setModel(songs);
        artistlist.setModel(artists);
        albumlist.setModel(albums);
    }
    
    private class OverlaySettings extends JFrame implements ActionListener
    {
        private final JButton okay;
        private final JCheckBox defaultEnabled;
        private final JLabel size, setDefault;
        private final JComboBox selectSize;
        private boolean isDefault;

        public OverlaySettings()
        {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridBagLayout());

            String[] theSizes = {"small", "medium", "large"};
            selectSize = new JComboBox(theSizes);
            okay = new JButton("Save");
            defaultEnabled = new JCheckBox();
            defaultEnabled.addActionListener(this);
            size = new JLabel("Overlay size:");
            setDefault = new JLabel("Overlay enabled by default:");

            okay.addActionListener(this);

            setSettings();

            makeGui();

        }

        private void setSettings()
        {
            try{
                BufferedReader red = new BufferedReader(new FileReader(IO.getDocumentsFolder() + "\\CowLite Audio Player\\resources\\launchersettings\\overlay.txt"));
                selectSize.setSelectedItem(red.readLine());

                isDefault = Boolean.parseBoolean(red.readLine());
                defaultEnabled.setSelected(isDefault);
                red.close();
            }catch(Exception e){}
        }

        private void makeGui()
        {
            GridBagConstraints c = new  GridBagConstraints();
            c.fill = c.BOTH;
            c.gridy = 1;
            c.gridx = 1;

            add(setDefault, c);

            c.gridx = 2;

            add(defaultEnabled, c);

            c.gridy = 2;

            add(selectSize, c);

            c.gridx = 1;

            add(size, c);

            c.gridwidth = 2;
            c.gridy = 3;
            c.fill = c.NONE;

            add(okay, c);

            pack();
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == okay)
            {
                setOverlaySettings((String)selectSize.getSelectedItem());
                tf.setVisible(isDefault);
                try{
                    PrintStream out = new PrintStream(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\launchersettings\\overlay.txt");
                    out.println((String)selectSize.getSelectedItem());
                    out.println(isDefault);
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }catch(Exception f){}
            }

            if(e.getSource() == defaultEnabled)
                isDefault = !isDefault;
        }
    }
    
    /**
     * (c) Copyright
     * A menu displaying all the currently selected colors for each GUI  item
     */
    private class GraphicalSettingsMenu implements ActionListener
    {
        private JLabel list, frameback, timeprim, timesec, volumeprim, volumesec,
                timelable, playlisttextlabel, menutextlabel, sliderbp, sliderbs, timelabel;
        private JButton listbtn, framebtn, timeprimbtn, timesecbtn, volumeprimbtn,
                volumesecbtn, timebtn, listtextbtn, sliderbackbtnprim, menutextbtn, sliderbackbtnsec;
        private JColorChooser chooser;
        private JFrame frame;
        
        private JButton save = new JButton("save");
        private JButton discard = new JButton("discard");
        private JButton activeButton;
        private Container cont;
        private JFrame colorframe;
        private boolean initialized = false;

        private final int BUTTONSIZE = 15;

        /**
         * creates a JFrame presenting all selected colors
         */
        public GraphicalSettingsMenu()
        {
            /*
            Here all objects are created. Each button is linked to a specific
            attribute of the GUI. If buttons are hit, the GraphicslSettingsListener
            will handle the recolor events.
            */
            frame = new  JFrame("Settings");
            frame.setSize(300,300);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocation(getLocation());

            volumeprimbtn = new JButton();
            listbtn = new JButton();
            framebtn = new JButton();
            timeprimbtn = new JButton();
            timesecbtn = new JButton();
            volumesecbtn = new JButton();
            timebtn = new JButton();
            listtextbtn = new JButton();
            menutextbtn = new JButton();
            sliderbackbtnprim = new JButton();
            sliderbackbtnsec = new JButton();

            makeButton(this, sliderbackbtnprim, timeUI.getBackColors()[0]);
            makeButton(this, sliderbackbtnsec, timeUI.getBackColors()[1]);
            makeButton(this, listbtn, LISTBG);
            makeButton(this, framebtn, BACKGROUND);
            makeButton(this, timeprimbtn, timeUI.getFillColors()[0]);
            makeButton(this, timesecbtn, timeUI.getFillColors()[1]);
            makeButton(this, volumesecbtn, VolumeSlider.fillColors[1]);
            makeButton(this, timebtn, timeUI.getTimecolor());
            makeButton(this, listtextbtn, PLAYLISTTEXT);
            makeButton(this, menutextbtn, MENUTEXTCOLOR);
            makeButton(this, volumeprimbtn, VolumeSlider.fillColors[0]);

            volumeprim = new JLabel("Primary Volumebar Color:     ");
            frameback = new JLabel("Frame Background:     ");
            list = new JLabel("Playlist Background:     ");
            playlisttextlabel = new JLabel("Playlist Text Color:     ");
            menutextlabel = new JLabel("Menu Text Color:    ");
            timeprim = new JLabel("Timebar Primary Color:     ");
            timesec = new JLabel("Timebar Secondary Color:     ");
            volumesec = new JLabel("Secondary Volumebar Color:     ");
            timelabel = new JLabel("Timer Color:     ");
            sliderbp = new JLabel("Primary Slider Background:     ");
            sliderbs = new JLabel("Secondary Slider Background:     ");

            GridBagConstraints c = new GridBagConstraints();
            frame.setLayout(new GridBagLayout());
            Container controller = frame.getContentPane();

            c = insertComponent(c, c.HORIZONTAL, 1, 1, 0, 0, 1, 1);
            controller.add(frameback, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 2, 0, 0, 1, 1);
            controller.add(list, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 3, 0, 0, 1, 1);
            controller.add(playlisttextlabel, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 4, 0, 0, 1, 1);
            controller.add(menutextlabel, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 5, 0, 0, 1, 1);
            controller.add(timeprim, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 6, 0, 0, 1, 1);
            controller.add(timesec, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 7, 0, 0, 1, 1);
            controller.add(volumeprim, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 8, 0, 0, 1, 1);
            controller.add(volumesec, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 9, 0, 0, 1, 1);
            controller.add(timelabel, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 10, 0, 0, 1, 1);
            controller.add(sliderbp, c);
            c = insertComponent(c, c.HORIZONTAL, 1, 11, 0, 0, 1, 1);
            controller.add(sliderbs, c);

            c = insertComponent(c, c.HORIZONTAL, 2, 1, 0, 0, 1, 1);
            controller.add(framebtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 2, 0, 0, 1, 1);
            controller.add(listbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 3, 0, 0, 1, 1);
            controller.add(listtextbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 4, 0, 0, 1, 1);
            controller.add(menutextbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 5, 0, 0, 1, 1);
            controller.add(timeprimbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 6, 0, 0, 1, 1);
            controller.add(timesecbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 7, 0, 0, 1, 1);
            controller.add(volumeprimbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 8, 0, 0, 1, 1);
            controller.add(volumesecbtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 9, 0, 0, 1, 1);
            controller.add(timebtn, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 10, 0, 0, 1, 1);
            controller.add(sliderbackbtnprim, c);
            c = insertComponent(c, c.HORIZONTAL, 2, 11, 0, 0, 1, 1);
            controller.add(sliderbackbtnsec, c);
            frame.setVisible(true);
        }

        private GridBagConstraints insertComponent(GridBagConstraints c, int fill, int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight)
        {
           c.fill = fill;
           c.gridx = gridx;
           c.gridy = gridy;
           c.weightx = weightx;
           c.weighty = weighty;
           c.gridwidth = gridwidth;
           c.gridheight = gridheight;
           return c;
        }

        private void makeButton(ActionListener reg, final JButton button, Color color)
        {
            button.addActionListener(reg);
            button.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
            button.setBackground(color);
        }
        
        /**
         * Gets called when a button has been pressed on the color-menu.
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {

            GridBagConstraints c = new GridBagConstraints();
            if(e.getSource() != save && e.getSource() != discard && !initialized)
            {
                //Adding actionlisteners to the save/discard buttons and notify that this has been initialized
                save.addActionListener(this);
                discard.addActionListener(this);
                initialized = true;

                //Creation of the colorpicker
                colorframe = new JFrame();
                colorframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                colorframe.setLayout(new GridBagLayout());
                colorframe.setSize(700, 400);
                colorframe.setLocation(200, 200);
                colorframe.setVisible(true);

                //Adding the save/discard buttons
                cont = colorframe.getContentPane();
                c = insertComponent(c, c.NONE, 1, 1, 0, 0, 1, 1);
                cont.add(save, c);
                c = insertComponent(c, c.NONE, 2, 1, 0, 0, 1, 1);
                cont.add(discard, c);

                //the selected button, we use this to determine what item's color we are changing
                activeButton = (JButton) e.getSource();

                c = insertComponent(c, c.BOTH, 1, 2, 0, 0, 2, 1);

                //Make a new colorchooser
                chooser = new JColorChooser(activeButton.getBackground());
                AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
                for(AbstractColorChooserPanel pane: panels)
                {
                    if(!pane.getDisplayName().equals("HSV") && !pane.getDisplayName().equals("HSL"))
                        chooser.removeChooserPanel(pane);
                }
                cont.add(chooser, c);
            }

            if(e.getSource() == save)
            {
                //Set the selected color by tracing back activeButton to it's original source
                activeButton.setBackground(chooser.getColor());
                if(activeButton == framebtn)
                    setNewBackground(chooser.getColor());
                if(activeButton == listbtn)
                    setListBG(chooser.getColor());
                if(activeButton == timeprimbtn)
                    timeUI.setPrimary(chooser.getColor());
                if(activeButton == timesecbtn)
                    timeUI.setSecondary(chooser.getColor());
                if(activeButton == timebtn)
                    timeUI.setTimeColor(chooser.getColor());
                if(activeButton == volumeprimbtn)
                    volumeUI.setPrimary(chooser.getColor());
                if(activeButton == volumesecbtn)
                    volumeUI.setSecondary(chooser.getColor());
                if(activeButton == listtextbtn)
                    setListFG(chooser.getColor());
                if(activeButton == menutextbtn)
                    setMenuTextColor(chooser.getColor());
                if(activeButton == sliderbackbtnprim)
                {
                    timeUI.setPrimaryBack(chooser.getColor());
                    volumeUI.setPrimaryBack(chooser.getColor());
                }
                if(activeButton == sliderbackbtnsec)
                {
                    timeUI.setSecondaryBack(chooser.getColor());
                    volumeUI.setSecondaryBack(chooser.getColor());
                }
            }
            if(e.getSource() == save || e.getSource() == discard)
            {
                initialized = false;
                colorframe.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                repaint();
            }
            chooser.setPreviewPanel(new JPanel());
        }
    }
}
