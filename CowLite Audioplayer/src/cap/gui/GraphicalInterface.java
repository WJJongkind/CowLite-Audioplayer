package cap.gui;

import cap.core.audio.FileAudioPlayer;
import cap.core.CowLiteAudioPlayer;
import static cap.core.CowLiteAudioPlayer.colorindex;
import cap.core.audio.ControlListener;
import cap.util.InterfaceIO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import sun.java2d.SunGraphicsEnvironment;

/**
 * (c) Copyright
 * The main GUI. Here all buttons, labels, frame-items etc. are created
 * & added to the interface of CowLite Audio Player.
 */
public class GraphicalInterface extends JFrame
{   public static DefaultListModel listmodel = new DefaultListModel();
    public static DefaultListModel savedmodel = new DefaultListModel();
    public static DefaultListModel songlistmodel = new DefaultListModel();
    public static DefaultListModel artistlistmodel = new DefaultListModel();
    public static DefaultListModel albumlistmodel = new DefaultListModel();
    public static JScrollPane playlist, savedLists;
    public static JList songlist, artistlist, albumlist, savedListText;
    public static JButton maximizeButton, exitButton, playButton, stopButton,
            prevButton, nextButton, clearButton, shuffleButton, alphabeticButton;
    public static JSlider volumeSlider, timeSlider;
    public static JSplitPane splitpane, songs, artists;
    public static JFileChooser filechooser;
    public static JMenuBar menufile;
    public static JMenu filemenu, helpmenu, settingsmenu;
    public static JMenuItem help, about, setHotkeys, saveList, removeList,
            setGraphics, setOverlay;
    public static JPanel top, left, right, bottom;
    public static String time;
    public static boolean uptodate = true;
    public static boolean uptodate2 = false;
    public static boolean maximized = false;
    public static Dimension oldDimension;
    public static Point oldPoint;
    public static GridBagConstraints c;
    
    public  static Color BACKGROUND = new Color(0x333333);
    public static Color PLAYLISTTEXT = new Color(0x333333);
    public static Color LISTBG = new Color(0x8E9191);
    public static Color MENUTEXTCOLOR = new Color(0x8E9191);
    public static final int SQUAREBUTTON = 20;
    public static final int RECTBUTTON = 15;
    
    /**
     * Adds all the components to the interface (this class)
     * @param title title of the JFrame
     */
    public GraphicalInterface(String title)
    {
        super(title);
        //Setting the correct color values (or uses defaults if not specified in saved file)
        try{
            BACKGROUND = colorindex.get(0);
            LISTBG = colorindex.get(1);
            PLAYLISTTEXT = colorindex.get(2);
            MENUTEXTCOLOR = colorindex.get(3);
        }catch(Exception e){}
        
        //For maximization
        GraphicsConfiguration config = getGraphicsConfiguration();
        Rectangle usableBounds = SunGraphicsEnvironment.getUsableBounds(config.getDevice());
        setMaximizedBounds(usableBounds);
        
        UIManager.put("SplitPane.background", BACKGROUND);
        this.setUndecorated(true);
        
        //For resizing the interface
        WindowMouseListener wndw = new WindowMouseListener();
        getContentPane().addMouseListener(wndw);
        getContentPane().addMouseMotionListener(wndw);
        
        //Creation of all components
        volumeSlider = new JSlider();
        setLayout(new GridBagLayout());
        volumeSlider.setOrientation(JSlider.VERTICAL);
        volumeSlider.setValue(75);
        SliderListener slistener = new SliderListener();
        volumeSlider.addChangeListener(slistener);
        volumeSlider.setBackground(BACKGROUND);
        volumeSlider.setUI(new VolumeSlider(){
            @Override
            protected void scrollDueToClickInTrack(int direction)
            {
                int value;
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    SliderListener.click = true;
                    slider.setValue(this.valueForXPosition(slider.getMousePosition().x));
                } else if (slider.getOrientation() == JSlider.VERTICAL) {
                    SliderListener.click = true;
                    slider.setValue(this.valueForYPosition(slider.getMousePosition().y));
                }
            }
        });
        
        timeSlider = new JSlider();
        SliderUI ui = timeSlider.getUI();
        timeSlider.setUI(new TimeSlider(){
            @Override
            protected void scrollDueToClickInTrack(int direction)
            {
                int value;
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    SliderListener.click = true;
                    slider.setValue(this.valueForXPosition(slider.getMousePosition().x));
                } else if (slider.getOrientation() == JSlider.VERTICAL) {
                    SliderListener.click = true;
                    slider.setValue(this.valueForYPosition(slider.getMousePosition().y));
                }
            }
        });
        timeSlider.setBackground(BACKGROUND);
        setLayout(new GridBagLayout());
        timeSlider.setOrientation(SwingConstants.HORIZONTAL);
        timeSlider.setValue(0);
        timeSlider.setMinimum(0); 
        timeSlider.addChangeListener(slistener);
        
        slistener = null;
        
        filechooser = new JFileChooser();
        filechooser.setCurrentDirectory(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\playlists\\"));
        
        makeMenu();
        
        ActionRegisterer rgstr = new ActionRegisterer();
        
        playButton = new JButton();
        makePlayButton(rgstr, playButton, SQUAREBUTTON, SQUAREBUTTON);
        stopButton = new JButton();
        makeButton(rgstr, stopButton, "stop", SQUAREBUTTON, SQUAREBUTTON);
        nextButton = new JButton();
        makeButton(rgstr, nextButton, "next", SQUAREBUTTON, SQUAREBUTTON);
        prevButton = new JButton();
        makeButton(rgstr, prevButton, "prev", SQUAREBUTTON, SQUAREBUTTON);
        clearButton = new JButton();
        makeButton(rgstr, clearButton, "clear", RECTBUTTON * 2 + 8, RECTBUTTON + 4);
        
        exitButton = new JButton();
        exitButton.addActionListener(rgstr);
        exitButton.setPreferredSize(new Dimension(35,30));
        exitButton.setMinimumSize(new Dimension(35, 30));
        exitButton.setMaximumSize(new Dimension(35, 30));
        exitButton.setFocusPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setOpaque(false);
        exitButton.setBackground(BACKGROUND);
        try{
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\" + "exit" + ".png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
            exitButton.setIcon(icon);
            play.flush();
        }catch(Exception e){System.out.println(e + "MainFrame Main");}
        maximizeButton = new JButton();
        maximizeButton.addActionListener(rgstr);
        maximizeButton.setPreferredSize(new Dimension(30,30));
        maximizeButton.setMinimumSize(new Dimension(30,30));
        maximizeButton.setMaximumSize(new Dimension(30,30));
        maximizeButton.setFocusPainted(false);
        maximizeButton.setContentAreaFilled(false);
        maximizeButton.setBorderPainted(false);
        maximizeButton.setOpaque(false);
        maximizeButton.setBackground(BACKGROUND);
        try{
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\" + "maximize" + ".png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
            maximizeButton.setIcon(icon);
            play.flush();
        }catch(Exception e){System.out.println(e + "MainFrame Main2");}

        
        shuffleButton = new JButton();
        shuffleButton.addActionListener(rgstr);
        shuffleButton.setPreferredSize(new Dimension((int)(RECTBUTTON / 0.75), RECTBUTTON));
        shuffleButton.setMinimumSize(new Dimension((int)(RECTBUTTON / 0.75), RECTBUTTON));
        shuffleButton.setMaximumSize(new Dimension((int)(RECTBUTTON / 0.75), RECTBUTTON));
        shuffleButton.setFocusPainted(false);
        shuffleButton.setContentAreaFilled(false);
        shuffleButton.setBorderPainted(false);
        shuffleButton.setBackground(BACKGROUND);
        shuffleButton.setOpaque(false);
        
        alphabeticButton = new JButton();
        alphabeticButton.addActionListener(rgstr);
        alphabeticButton.setPreferredSize(new Dimension((int)(RECTBUTTON / 0.65), RECTBUTTON));
        alphabeticButton.setMinimumSize(new Dimension((int)(RECTBUTTON / 0.65), RECTBUTTON));
        alphabeticButton.setMaximumSize(new Dimension((int)(RECTBUTTON / 0.65), RECTBUTTON));
        alphabeticButton.setFocusPainted(false);
        alphabeticButton.setContentAreaFilled(false);
        alphabeticButton.setBorderPainted(false);
        alphabeticButton.setBackground(BACKGROUND);
        alphabeticButton.setOpaque(false);
        
        
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
        
        InterfaceIO.setShuffle();
        InterfaceIO.setAlphabetic();
        
        Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
        savedListText = new JList(savedmodel);
        savedListText.addKeyListener(new ControlListener());
        savedLists = new JScrollPane(savedListText);
        savedLists.setBorder(null);
        savedLists.setViewportBorder(border);
        savedLists.setBorder(border);
        savedListText.setBackground(LISTBG);
        addSavedListListener();
        
        songlist = new JList(songlistmodel);
        artistlist = new JList(artistlistmodel);
        albumlist = new JList(albumlistmodel);
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
    public static GridBagConstraints insertComponent(GridBagConstraints c, int fill, int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight)
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
    
    private void makeButton(ActionRegisterer reg, final JButton button, String type, int x, int y)
    {
        //Make the button
        button.addActionListener(reg);
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
            Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\" + type + ".png"));
            ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
            button.setIcon(icon);
            play.flush();
        }catch(Exception e){System.out.println(e + "makebutton");}
        
        //Change icon upon click events
        button.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                try{
                Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\" + type + "pressed.png"));
                ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
                button.setIcon(icon);
                play.flush();
                }catch(Exception f){System.out.println(f + "mousepressed");}
            }
            
            public void mouseReleased(MouseEvent e)
            {
                try{
                Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\" + type + ".png"));
                ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
                button.setIcon(icon);
                play.flush();
                }catch(Exception f){System.out.println(f + "mousereleased");}
            }
        });
    }
    
    private void makePlayButton(ActionRegisterer reg, final JButton button, int x, int y)
    {
        //make the button
        button.addActionListener(reg);
        button.setPreferredSize(new Dimension(x,y));
        button.setMinimumSize(new Dimension(x,y));
        button.setMaximumSize(new Dimension(x,y));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(BACKGROUND);
        
        InterfaceIO.setPlayButton();
        
        button.addMouseListener(new MouseAdapter(){
           
            //Sets the correct image for the playbutton (pause or play image)
            public void mousePressed(MouseEvent e)
            {
                if(!InterfaceIO.pauseSet)
                {
                    try{
                    Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\playpressed.png"));
                    ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
                    button.setIcon(icon);
                    play.flush();
                    }catch(Exception f){System.out.println(f);}
                }
                else
                {
                    try{
                    Image play = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\pausepressed.png"));
                    ImageIcon icon = new ImageIcon(play.getScaledInstance(x - (x / 10),y - (y / 10),50));
                    button.setIcon(icon);
                    play.flush();
                    }catch(Exception f){System.out.println(f);}
                }
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
                            
                    
                    if(songlist.getSelectedIndex() > -1 && songlist.getSelectedIndex() != CowLiteAudioPlayer.player.getIndex())
                        CowLiteAudioPlayer.player.selectSong(songlist.getSelectedIndex());
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

            private int oldSelected = -1;
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if(savedListText.getSelectedIndex() == 0)
                {
                    CowLiteAudioPlayer.player.stop();
                    CowLiteAudioPlayer.player.clearList();
                    time = "";
                    timeSlider.setValue(0);
                    uptodate = false;
                    uptodate2 = false;
                }
                if(savedListText.getSelectedIndex() > -1)
                {
                    songlist.clearSelection();
                    albumlist.clearSelection();
                    artistlist.clearSelection();
                }
                if((savedListText.getSelectedIndex() != -1 && oldSelected != savedListText.getSelectedIndex()) || CowLiteAudioPlayer.player.getList().size() == 0)
                {
                    oldSelected = savedListText.getSelectedIndex();
                    CowLiteAudioPlayer.player.clearList();
                    CowLiteAudioPlayer.player.loadList(CowLiteAudioPlayer.playlists.get(savedListText.getSelectedIndex()));
                }
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
        filemenu.add(saveList);
        MenuListener menulistener = new MenuListener();
        setGraphics.addActionListener(menulistener);
        saveList.addActionListener(menulistener);
        removeList.addActionListener(menulistener);
        filemenu.add(removeList);
        filemenu.setForeground(MENUTEXTCOLOR);
        menufile.add(filemenu);
        //add(menu);
        
        settingsmenu = new JMenu("settings");
        setHotkeys = new JMenuItem("Set Hotkeys");
        setHotkeys.addActionListener(menulistener);
        setOverlay = new JMenuItem("Change Overlay");
        setOverlay.addActionListener(menulistener);
        settingsmenu.add(setHotkeys);
        settingsmenu.add(setGraphics);
        settingsmenu.add(setOverlay);
        settingsmenu.setForeground(MENUTEXTCOLOR);
        menufile.add(settingsmenu);
        
        helpmenu = new JMenu("help");
        help = new JMenuItem("settings & controls");
        about = new JMenuItem("about...");
        about.addActionListener(menulistener);
        help.addActionListener(menulistener);
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
    public static void setNewBackground(Color c)
    {
        System.out.println(c);
        System.out.println(BACKGROUND);
        BACKGROUND = c;
        GUIHandler.frame.getContentPane().setBackground(c);
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
    public static void setMenuTextColor(Color c)
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
    public static void setListBG(Color c)
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
    public static void setListFG(Color c)
    {
        PLAYLISTTEXT = c;
        
        songlist.setForeground(c);
        artistlist.setForeground(c);
        albumlist.setForeground(c);
        savedListText.setForeground(c);
        //playlist.setForeground(c);
        savedLists.setForeground(c);
    }
}
