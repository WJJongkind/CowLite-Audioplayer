/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.control.GlobalKeyListener;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 *
 * @author Wessel
 */
public class OverlayListener implements MouseListener, MouseMotionListener
{
    private Point previous, onOverlayClick;
    private InfoComponent info;
    private Robot robot;
    private boolean clicked;
    
    public OverlayListener(InfoComponent info)
    {
        this.info = info;
        try{
            robot = new Robot();
        }catch(Exception e){}
        clicked = false;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(!GlobalKeyListener.alt && !info.isReady())
        {
            if(onOverlayClick == null)
                onOverlayClick = e.getPoint();
            clicked = false;
            info.hide(true);
            robot.mouseMove(onOverlayClick.x, onOverlayClick.y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        else
        {
            if(info.isReady())
            {
                if(!clicked)
                {
                    robot.mouseMove(onOverlayClick.x, onOverlayClick.y);
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    robot.mouseMove(e.getPoint().x, e.getPoint().y);
                    onOverlayClick = null;
                    clicked = true;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        previous = null;
        
        if(clicked)
            info.hide(false);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(GlobalKeyListener.alt)
        {
            Point p = e.getPoint();
            if(previous == null)
                previous = p;
            else
            {
                int dx = p.x - previous.x;
                int dy = p.y - previous.y;

                info.changeOffsetX(dx);
                info.changeOffsetY(dy);
                
                previous = p;
            }
        }
        else
            previous = null;
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        
    }
}
