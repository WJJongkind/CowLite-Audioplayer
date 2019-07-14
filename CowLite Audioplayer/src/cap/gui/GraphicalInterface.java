package cap.gui;

import cap.core.PropertiesManager;
import cap.core.audio.PlaylistPlayer;
import java.util.Map;
import javax.swing.*;

/**
 * (c) Copyright
 * The main GUI. Here all buttons, labels, frame-items etc. are created
 * & added to the interface of CowLite Audio Player.
 */
public class GraphicalInterface extends JFrame
{
    private JMenuBar menufile;
    private JMenu filemenu, helpmenu, settingsmenu;
    private JMenuItem help, about, setHotkeys, saveList, removeList, importYoutube,
            setGraphics, setOverlay;
  
    /**
     * Adds all the components to the interface (this class)
     * @param title title of the JFrame
     */
    public GraphicalInterface() throws Exception
    {
      
    }
    
    
//    /**
//     * Makes the menubar
//     */
//    private void makeMenu()
//    {
//        setGraphics = new JMenuItem("Change Layout");
//        menufile = new JMenuBar();
//        menufile.setBorder(null);
//        filemenu = new JMenu("file");
//        saveList = new JMenuItem("Save Playlist");
//        removeList = new JMenuItem("Remove Selected Playlist");
//        importYoutube = new JMenuItem("Import YouTube Playlist");
//        filemenu.add(saveList);
//        setGraphics.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new GraphicalSettingsMenu();
//            }
//        });
//        saveList.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(AUDIO.getPlayer() instanceof FileAudioPlayer)
//                {
//                    try{
//                        DefaultWindow window = new DefaultWindow(GRAPHICS);
//                        window.setSize(400, 300);
//                        window.setContentPanel(new SavePanel(GRAPHICS, AUDIO));
//                        window.setVisible(true);
//                    }catch(Exception f){
//                        f.printStackTrace();
//                    }
//                }
//            }
//        });
//        removeList.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                IO.removePlaylist(AUDIO, CONTROLLER, AUDIO.getPlaylistPath((String) savedListText.getSelectedValue()));
//                savedListText.remove(savedListText.getSelectedIndex());
//            }
//        });
//        importYoutube.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try{
//                    DefaultWindow window = new DefaultWindow(GRAPHICS);
//                    window.setContentPanel(new YTImportPanel(GRAPHICS, AUDIO));
//                    window.setSize(400, 300);
//                    window.setVisible(true);
//                }catch(Exception f){
//                    f.printStackTrace();
//                }
//            }
//        });
//        filemenu.add(removeList);
//        filemenu.add(importYoutube);
//        filemenu.setForeground(MENUTEXTCOLOR);
//        menufile.add(filemenu);
//        //add(menu);
//        
//        settingsmenu = new JMenu("settings");
//        setHotkeys = new JMenuItem("Set Hotkeys");
//        setHotkeys.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e){
//                new SettingsMenu(PROPERTIES);
//            }
//        });
//        
//        setOverlay = new JMenuItem("Change Overlay");
//        setOverlay.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new OverlaySettings();
//            }
//        });
//        settingsmenu.add(setHotkeys);
//        settingsmenu.add(setGraphics);
//        settingsmenu.add(setOverlay);
//        settingsmenu.setForeground(MENUTEXTCOLOR);
//        menufile.add(settingsmenu);
//        
//        helpmenu = new JMenu("help");
//        help = new JMenuItem("settings & controls");
//        about = new JMenuItem("about...");
//        about.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try{
//                    Desktop.getDesktop().edit(new File(IO.getDocumentsFolder() + "\\resources\\infofiles\\about.txt"));
//                }catch(Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });
//        help.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try{
//                    Desktop.getDesktop().edit(new File(IO.getDocumentsFolder() + "\\resources\\infofiles\\controls.txt"));
//                }catch(Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });
//        helpmenu.add(help);
//        helpmenu.add(about);
//        menufile.add(helpmenu);
//        helpmenu.setForeground(MENUTEXTCOLOR);
//        menufile.setEnabled(false);
//        menufile.setBackground(BACKGROUND);
//    } 
//    
//    public void updateOverlay(String time, String song, double volume)
//    {
//        InfoComponent info = tf.getInfoComponent();
//        info.setSong(song);
//        info.setTime(time);
//        info.setVolume(volume);
//        tf.repaint();
//    }
//
//  
//    public void toggleOverlay()
//    {
//        tf.setVisible(!tf.isVisible());
//    }
//
//    public void setOverlaySettings(String size)
//    {
//        tf.getInfoComponent().setActiveSizes(size);
//    }
}
