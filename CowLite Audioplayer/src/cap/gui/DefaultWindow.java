package cap.gui;

import cap.core.PropertiesManager;
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


public class DefaultWindow extends JFrame
{
    private boolean maximized = false;
    private final JButton exitButton, maximizeButton;
    private final JPanel contentHolder;
    private JPanel content;
    private Dimension oldDimension;
    private Point oldPoint;
    private static ImageIcon exit, maximize;
    
    public static int CONTENT_START_X = 2, CONTENT_START_Y = 2, CONTENT_END_X = 3, CONTENT_END_Y = 3;
    
   /* public static void main(String[] args) throws Exception
    {
        PropertiesManager manager = new PropertiesManager();
        DefaultWindow window = new DefaultWindow(manager.getGraphicsProperties());
        window.setSize(1280, 720);
        window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
        window.setContentPanel(new YTImportPanel(manager.getGraphicsProperties()));
        window.setVisible(true);
    }*/
    
    public DefaultWindow(Map<String, Object> graphics) throws Exception
    {
        GraphicsConfiguration config = getGraphicsConfiguration();
        Rectangle usableBounds = SunGraphicsEnvironment.getUsableBounds(config.getDevice());
        setMaximizedBounds(usableBounds);
        
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setLayout(new GridBagLayout());
        
        DefaultWindowMouseListener listener = new DefaultWindowMouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
        
        exitButton = new JButton();
        JFrame src = this;
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
        exitButton.setBackground((Color)graphics.get("background"));
        
        if(this.exit == null)
        {
            Image exit = ImageIO.read(new File(IO.getDocumentsFolder() + "\\resources\\graphics\\" + "exit" + ".png"));
            this.exit = new ImageIcon(exit.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
        }
        exitButton.setIcon(this.exit);
        
            
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
        maximizeButton.setBackground((Color) graphics.get("background"));
        
        if(this.maximize == null)
        {
            Image maximizeImage = ImageIO.read(new File(IO.getDocumentsFolder() + "\\resources\\graphics\\" + "maximize" + ".png"));
            this.maximize = new ImageIcon(maximizeImage.getScaledInstance(12 - (12 / 10),12 - (12 / 10),50));
        }
        maximizeButton.setIcon(this.maximize);
        
        contentHolder = new JPanel();
        contentHolder.setBackground((Color) graphics.get("listbg"));
        
        //Add components to the interface
        Container controller = getContentPane();
        controller.setBackground((Color) graphics.get("background"));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.RELATIVE;
        
        JPanel topLeft = new JPanel();
        topLeft.setMinimumSize(new Dimension(30, 30));
        topLeft.setMaximumSize(new Dimension(30, 30));
        topLeft.setPreferredSize(new Dimension(30, 30));
        topLeft.setSize(new Dimension(30, 30));
        topLeft.setBackground((Color) graphics.get("background"));
        
        JPanel bottomLeft = new JPanel();
        bottomLeft.setMinimumSize(new Dimension(30, 30));
        bottomLeft.setMaximumSize(new Dimension(30, 30));
        bottomLeft.setPreferredSize(new Dimension(30, 30));
        bottomLeft.setSize(new Dimension(30, 30));
        bottomLeft.setBackground((Color) graphics.get("background"));
        
        c.fill = c.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(topLeft, c);
        
        c.gridx++;
        
        c.anchor = GridBagConstraints.LINE_END;
        
        add(maximizeButton, c);
        
        c.gridx++;
        
        c.anchor = c.CENTER;
        
        add(exitButton, c);
        
        c.gridy++;
        c.gridx = 2;
        c.fill = c.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        
        add(contentHolder, c);
        
        c.gridy++;
        c.gridx = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = c.NONE;
        
        add(bottomLeft, c);
    }
    
    public void setContentPanel(DefaultPanel content)
    {
        contentHolder.removeAll();
        contentHolder.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.BOTH;
        
        contentHolder.add(content, c);
        content.setParent(this);
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
    
    public void exit()
    {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
