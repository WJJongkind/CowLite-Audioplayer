package cap.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wessel
 */
public class DefaultWindowMouseListener implements MouseMotionListener, MouseListener
{
    private boolean resizing = false;
    private Point initialclick;
    public static final int B = 7;
    
    //minimum sizes
    public static final int W = 579;
    public static final int H = 264;
    
    //The coordinates on the frame. se/sw/ne/nw are the corners, n/ee/w/s/ are top, right, left, bottom
    private boolean se = false, sw = false, ne = false, nw = false, n = false, ee = false, w = false, s = false;
    private int addXC, addYC, addXZ, addYZ;

    private final DefaultWindow window;
    
    public DefaultWindowMouseListener(DefaultWindow frame)
    {
        this.window = frame;
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        initialclick = e.getPoint();
    }

    /**
     * back to the default cursor
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        JFrame frame = window;
        frame.repaint();
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        resizing = false;
        se = false; sw = false; ne = false; nw = false; n = false; ee = false; w = false; s = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * back to the default cursor
     * @param e 
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        JFrame frame = window;
        if(!resizing)
            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Mouse gets dragged, window has to relocate or resize
     * @param e 
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        try
        {
            JFrame frame = window;
            Point p = e.getPoint();
            int x = (int)(frame.getX() + (p.getX() - initialclick.getX()));
            int y = (int)(frame.getY() + (p.getY() - initialclick.getY()));
            if(!resizing && initialclick.getX() > B && initialclick.getX() < frame.getWidth() - B && initialclick.getY() > B && initialclick.getY() < frame.getHeight() - B)
            {
                frame.setLocation(x, y);
                window.setOldLocation(frame.getLocation());
            }
            else
            {
                resize((int)initialclick.getX(),(int)initialclick.getY(), e);
                changeFrame();
                if(frame.getWidth() < W)
                    frame.setSize(579,frame.getHeight());
                if(frame.getHeight() < H)
                    frame.setSize(frame.getWidth(),264);
                window.setOldSize(frame.getSize());
            }
        }catch(Exception f){/*System.out.println(f);*/}
    }
    
    private void changeFrame()
    {
        JFrame frame = window;
        frame.setSize(frame.getSize().width + addXZ, frame.getSize().height + addYZ);
        frame.setLocation(frame.getLocation().x + addXC, frame.getLocation().y + addYC);
    }
    
    /**
     * Sets the correct values for frame resizing. Overly complex method, but hey, it works.
     * @param x location x of where the mouse since the last click/resize was
     * @param y location y of where the mouse since the last click/resize was
     * @param e the mouse event
     */
    private void resize(int x, int y, MouseEvent e)
    {
        JFrame frame = window;
        
        //We're resizing
        resizing = true;
        
        /*
        corrected values. dx/dy is the difference in cursor location between
        the last time this method got called and now. x and y are the old
        cursor locations. ix and iy are the new (Input) cursor locations.
        newx/newy will be used to set the new sizes. They are redundant though.
        */
        int dx = (int)e.getPoint().getX() - x;
        int dy = (int)e.getPoint().getY() - y;
        int ix = (int)e.getPoint().getX();
        int iy = (int)e.getPoint().getY();
        int oldw = frame.getWidth();
        int oldh = frame.getHeight();
        int newx = oldw;
        int newy = oldh;
        
        if((x >= frame.getWidth() - B && y >= frame.getHeight() - B && !sw && !ne && !nw && !n && !ee && !s && !w ) || se)
        {
            se = true;
            if(frame.getWidth() > W || dx > 0)
            {
                addXZ = dx;
                initialclick = new Point(x + dx, (int)initialclick.getY());
            }
            if(frame.getHeight() > H || dy > 0)
            {
                addYZ = dy;
                initialclick = new Point((int)initialclick.getX(), y + dy);
            }
            return;
        }
        if((x < B && y < B && !sw && !ne && !se && !n && !ee && !s && !w ) || nw)
        {
            nw = true;
                if(frame.getWidth() > W || ix < 0)
                {
                    addXZ = -1*ix;
                    addXC = ix;
                    initialclick = new Point(x + -1*dx, (int)initialclick.getY());
                }
                if(frame.getHeight() > H || iy < 0)
                {
                    addYZ = -1*iy;
                    addYC = iy;
                    initialclick = new Point((int)initialclick.getX(), y + -1*dy);
                }
                return;
            }
        
        if((x < B && y >= frame.getHeight() - B && !nw && !ne && !se && !n && !ee && !s && !w ) || sw)
        {
            sw = true;
            if(frame.getWidth() > W || ix < 0)
            {
                addXZ = -1*ix;
                addXC = ix;
                newx = -1*ix + newx;
                initialclick = new Point(x + -1*dx, (int)initialclick.getY());
            }
            if(frame.getHeight() > H || iy > frame.getHeight() - B)
            {
                addYZ = dy;
                newy =  dy + newy;
                initialclick = new Point(x, y + dy);
            }
            return;
        }
        if((x >= frame.getWidth() - B && y < B && !nw && !sw && !se && !n && !ee && !s && !w ) || ne)
        {
            ne = true;
            if(frame.getWidth() > W || ix > frame.getWidth() - B)
            {
                addXZ = dx;
                newx = dx + newx;
                initialclick = new Point(x + dx, y);
            }
            if(frame.getHeight() > H || iy < 0)
            {
                addYZ = -1*dy;
                addYC = dy;
                newy =  -1*dy + newy;
                initialclick = new Point(x + dx, y);
            }
            return;
        }
        if((x >= frame.getWidth() - B && !nw && !ne && !se && !n && !sw && !s && !w ) || ee)
        {
            initialclick = new Point(x + dx, y);
            ee = true;
            if(frame.getWidth() > W || ix > frame.getWidth())
            {
                addXZ = dx;
                initialclick = new Point(x + dx, (int)initialclick.getY());
            }
            return;
        }
        if((y >= frame.getHeight() - B && !nw && !ne && !se && !n && !sw && !ee && !w ) || s)
        {
            initialclick = new Point(x, y + dy);
            s = true;
            
            if(frame.getHeight() > H || y > frame.getHeight()-B)
            {
                addYZ = dy;
                initialclick = new Point((int)initialclick.getX(), y + dy);
            }
            return;
        }
        if((y < B && !nw && !ne && !se && !ee && !sw && !s && !w ) || n)
        {
            n = true;
            if(frame.getHeight() > H || iy < 0)
            {
                addYZ = -1*iy;
                addYC = iy;
                initialclick = new Point((int)initialclick.getX(), iy + -1*dy);
            }
            else
            return;
        }
        if((x < B && !nw && !ne && !se && !n && !ee && !s && !sw ) || w)
        {
            w = true;
            if(frame.getWidth() > W || ix < 0)
            {
                addXZ = -1*ix;
                addXC = ix;
                initialclick = new Point(x + -1*dx, (int)initialclick.getY());
            }
            return;
        }
    }
            

    /**
     * Set the correct cursor when the mouse hovers around the frame
     * @param e mouse event
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        JFrame frame = window;
        
        int x = (int) e.getPoint().getX();
        int y = (int) e.getPoint().getY();
        
        if(x >= frame.getWidth() - B && y >= frame.getHeight() - B)
        {
            frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            return;
        }
        if(x < B && y < B)
        {
            frame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            return;
        }
        if(x < B && y >= frame.getHeight() - B)
        {
            frame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
            return;
        }
        if(x >= frame.getWidth() - B && y < B)
        {
            frame.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
            return;
        }
        if(x >= frame.getWidth() - B)
        {
            frame.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
            return;
        }
        if(y >= frame.getHeight() - B)
        {
            frame.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
            return;
        }
        if(y < B)
        {
            frame.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
            return;
        }
        if(x < B)
        {
            frame.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
            return;
        }
        if(!resizing)
            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
}
