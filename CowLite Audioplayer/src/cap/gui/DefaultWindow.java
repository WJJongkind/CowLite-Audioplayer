package cap.gui;

import cap.util.IO;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.java2d.SunGraphicsEnvironment;


public class DefaultWindow
{
    private boolean maximized = false;
    private final JFrame window;
    private JButton exitButton, maximizeButton;
    private JPanel content;
    private Dimension oldDimension;
    private Point oldPoint;
    private Color BACKGROUND = new Color(0x333333);
    private Color FOREGROUND = new Color(0x333333);
    private Color CONTENTBG = new Color(0x8E9191);
    private Color MENUTEXTCOLOR = new Color(0x8E9191);
    private static ImageIcon exit, maximize;
    
    public static int CONTENT_START_X = 2, CONTENT_START_Y = 2, CONTENT_END_X = 3, CONTENT_END_Y = 3;
    
    public DefaultWindow() throws Exception
    {
        window = new JFrame();
        //Setting the correct color values (or uses defaults if not specified in saved file)
        /*try{
            BACKGROUND = (Color) GRAPHICS.get("background");
            LISTBG = (Color) GRAPHICS.get("listbg");
            PLAYLISTTEXT = (Color) GRAPHICS.get("listtext");
            MENUTEXTCOLOR = (Color) GRAPHICS.get("menutext");
        }catch(Exception e){}*/
        
        //For maximization
        GraphicsConfiguration config = window.getGraphicsConfiguration();
        Rectangle usableBounds = SunGraphicsEnvironment.getUsableBounds(config.getDevice());
        window.setMaximizedBounds(usableBounds);
        
        window.setUndecorated(true);
        
        //For resizing the interface
        //DefaultWindowMouseListener wndw = new DefaultWindowMouseListener(this);
       // window.getContentPane().addMouseListener(wndw);
       // window.getContentPane().addMouseMotionListener(wndw);
        
        window.setLayout(new GridBagLayout());
        
        JFrame src = window;
        exitButton = new JButton();
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispatchEvent(new WindowEvent(src, WindowEvent.WINDOW_CLOSING));
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
        
        if(this.exit == null)
        {
            Image exit = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + "exit" + ".png"));
            this.exit = new ImageIcon(exit.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
        }
        exitButton.setIcon(this.exit);
        
            
        maximizeButton = new JButton();
        maximizeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!maximized)
                {
                    oldDimension = new Dimension(window.getWidth(), window.getHeight());
                    oldPoint = new Point(window.getLocation());
                    window.setExtendedState(MAXIMIZED_BOTH); 
                    maximized = true;
                }
                else
                {
                    maximized = false;
                    window.setSize(oldDimension);
                    window.setLocation(oldPoint);
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
        
        if(this.maximize == null)
        {
            Image maximizeImage = ImageIO.read(new File(IO.getDocumentsFolder() + "CowLite Audio Player\\resources\\graphics\\" + "maximize" + ".png"));
            this.maximize = new ImageIcon(maximizeImage.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
        }
        maximizeButton.setIcon(this.maximize);
        
        content = new JPanel();
        content.setBackground(CONTENTBG);
        
        //Add components to the interface
        Container controller = window.getContentPane();
        controller.setBackground(BACKGROUND);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.RELATIVE;
        
        /*
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
        */
        JPanel topLeft = new JPanel();
        topLeft.setMinimumSize(new Dimension(30, 30));
        topLeft.setMaximumSize(new Dimension(30, 30));
        topLeft.setPreferredSize(new Dimension(30, 30));
        topLeft.setSize(new Dimension(30, 30));
        topLeft.setBackground(BACKGROUND);
        
        JPanel bottomLeft = new JPanel();
        bottomLeft.setMinimumSize(new Dimension(30, 30));
        bottomLeft.setMaximumSize(new Dimension(30, 30));
        bottomLeft.setPreferredSize(new Dimension(30, 30));
        bottomLeft.setSize(new Dimension(30, 30));
        bottomLeft.setBackground(BACKGROUND);
        
        c.fill = c.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        window.add(topLeft, c);
        
        c.gridx++;
        
        c.anchor = GridBagConstraints.LINE_END;
        
        window.add(maximizeButton, c);
        
        c.gridx++;
        
        c.anchor = c.CENTER;
        
        window.add(exitButton, c);
        
        c.gridy++;
        c.gridx = 2;
        c.fill = c.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        
        window.add(content, c);
        
        c.gridy++;
        c.gridx = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = c.NONE;
        
        window.add(bottomLeft, c);
    }
    
    public DefaultWindow(Map<String, Object> graphics)
    {
        window = new JFrame();
    }
    
    public JFrame getWindow()
    {
        return window;
    }
    
    public JPanel getContentPanel()
    {
        return content;
    }
    
    public void setOldSize(Dimension size)
    {
        oldDimension = size;
    }
    
    public void setOldLocation(Point location)
    {
        oldPoint = location;
    }
}
