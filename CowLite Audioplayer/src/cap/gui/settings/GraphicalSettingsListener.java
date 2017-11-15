package cap.gui.settings;

import cap.gui.GUIHandler;
import cap.gui.GraphicalInterface;
import cap.gui.TimeSlider;
import cap.gui.VolumeSlider;
import static cap.gui.settings.GraphicalSettingsMenu.*;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * (c) Copyright
 * Listens for which color the user wants to change.
 */
public class GraphicalSettingsListener implements ActionListener
{
    public static JButton save = new JButton("save");
    public static JButton discard = new JButton("discard");
    private JButton activeButton;
    public static Container cont;
    public static JFrame colorframe;
    private boolean initialized = false;
    
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
            colorframe.setLocation(GUIHandler.frame.getLocation());
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
                GraphicalInterface.setNewBackground(chooser.getColor());
            if(activeButton == listbtn)
                GraphicalInterface.setListBG(chooser.getColor());
            if(activeButton == timeprimbtn)
                TimeSlider.setPrimary(chooser.getColor());
            if(activeButton == timesecbtn)
                TimeSlider.setSecondary(chooser.getColor());
            if(activeButton == timebtn)
                TimeSlider.setTimeColor(chooser.getColor());
            if(activeButton == volumeprimbtn)
                VolumeSlider.setPrimary(chooser.getColor());
            if(activeButton == volumesecbtn)
                VolumeSlider.setSecondary(chooser.getColor());
            if(activeButton == listtextbtn)
                GraphicalInterface.setListFG(chooser.getColor());
            if(activeButton == menutextbtn)
                GraphicalInterface.setMenuTextColor(chooser.getColor());
            if(activeButton == sliderbackbtnprim)
            {
                TimeSlider.setPrimaryBack(chooser.getColor());
                VolumeSlider.setPrimaryBack(chooser.getColor());
            }
            if(activeButton == sliderbackbtnsec)
            {
                TimeSlider.setSecondaryBack(chooser.getColor());
                VolumeSlider.setSecondaryBack(chooser.getColor());
            }
        }
        if(e.getSource() == save || e.getSource() == discard)
        {
            initialized = false;
            colorframe.dispatchEvent(new WindowEvent(GUIHandler.frame, WindowEvent.WINDOW_CLOSING));
            GUIHandler.frame.repaint();
        }
        chooser.setPreviewPanel(new JPanel());
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
}
