package cap.gui.settings;

import cap.gui.GUIHandler;
import cap.gui.GraphicalInterface;
import cap.gui.TimeSlider;
import cap.gui.VolumeSlider;
import cap.gui.overlay.InfoComponent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * (c) Copyright
 * A menu displaying all the currently selected colors for each GUI  item
 */
public class GraphicalSettingsMenu
{
    public static JLabel list, frameback, timeprim, timesec, volumeprim, volumesec,
            timelable, playlisttextlabel, menutextlabel, sliderbp, sliderbs, timelabel, 
            overlaytext1, overlaytext2, overlaytext3;
    public static JButton listbtn, framebtn, timeprimbtn, timesecbtn, volumeprimbtn,
            volumesecbtn, timebtn, listtextbtn, sliderbackbtnprim, menutextbtn, sliderbackbtnsec,
            overlaytext1btn, overlaytext2btn, overlaytext3btn;
    public static JColorChooser chooser;
    private JFrame frame;
    
    public static final int BUTTONSIZE = 15;
    
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
        frame.setLocation(GUIHandler.frame.getLocation());
        
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
        overlaytext1btn = new JButton();
        overlaytext2btn = new JButton();
        overlaytext1btn = new JButton();
        
        ActionListener listener = new GraphicalSettingsListener();
        makeButton(listener, sliderbackbtnprim, TimeSlider.backColors[0]);
        makeButton(listener, sliderbackbtnsec, TimeSlider.backColors[1]);
        makeButton(listener, listbtn, GraphicalInterface.LISTBG);
        makeButton(listener, framebtn, GraphicalInterface.BACKGROUND);
        makeButton(listener, timeprimbtn, TimeSlider.fillColors[0]);
        makeButton(listener, timesecbtn, TimeSlider.fillColors[1]);
        makeButton(listener, volumesecbtn, VolumeSlider.fillColors[1]);
        makeButton(listener, timebtn, TimeSlider.timecolor);
        makeButton(listener, listtextbtn, GraphicalInterface.PLAYLISTTEXT);
        makeButton(listener, menutextbtn, GraphicalInterface.MENUTEXTCOLOR);
        makeButton(listener, volumeprimbtn, VolumeSlider.fillColors[0]);
        makeButton(listener, overlaytext1btn, InfoComponent.text1);
        makeButton(listener, overlaytext2btn, InfoComponent.text2);
        
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
        overlaytext1 = new JLabel("1st Overlay Text Color:");
        overlaytext2 = new JLabel("2nd Overlay Text Color:");
        overlaytext3 = new JLabel("3rd Overlay Text Color:");
        
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
        c = insertComponent(c, c.HORIZONTAL, 1, 12, 0, 0, 1, 1);
        controller.add(overlaytext1, c);
        c = insertComponent(c, c.HORIZONTAL, 1, 13, 0, 0, 1, 1);
        controller.add(overlaytext2, c);
        c = insertComponent(c, c.HORIZONTAL, 1, 14, 0, 0, 1, 1);
        controller.add(overlaytext3, c);
        
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
        c = insertComponent(c, c.HORIZONTAL, 2, 12, 0, 0, 1, 1);
        controller.add(overlaytext1btn, c);
        c = insertComponent(c, c.HORIZONTAL, 2, 13, 0, 0, 1, 1);
        controller.add(overlaytext2btn, c);
        c = insertComponent(c, c.HORIZONTAL, 2, 14, 0, 0, 1, 1);
        controller.add(overlaytext1btn, c);
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
}
