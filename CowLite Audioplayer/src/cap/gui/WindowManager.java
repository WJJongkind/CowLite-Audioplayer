package cap.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * The ComponentResizer allows you to resize a component by dragging a border of
 * the component.
 */
public class WindowManager extends MouseAdapter {

    // MARK: - Associated types & constants
    
    private static final Map<Integer, Integer> cursors = new HashMap<Integer, Integer>();
    static {
        cursors.put(1, Cursor.N_RESIZE_CURSOR);
        cursors.put(2, Cursor.W_RESIZE_CURSOR);
        cursors.put(4, Cursor.S_RESIZE_CURSOR);
        cursors.put(8, Cursor.E_RESIZE_CURSOR);
        cursors.put(3, Cursor.NW_RESIZE_CURSOR);
        cursors.put(9, Cursor.NE_RESIZE_CURSOR);
        cursors.put(6, Cursor.SW_RESIZE_CURSOR);
        cursors.put(12, Cursor.SE_RESIZE_CURSOR);
    }
    
    private static final int NORTH = 1;
    private static final int WEST = 2;
    private static final int SOUTH = 4;
    private static final int EAST = 8;
    
    private static final Insets dragInsets = new Insets(5, 5, 5, 5);
    private static final int moveInset = 10;
    
    private enum State {
        inactive,
        resizing,
        moving
    }

    // MARK: - Private properties
    
    private int direction;
    private Cursor sourceCursor;
    private State state = State.inactive;
    private Rectangle bounds;
    private Point pressed;
    private boolean autoscrolls;
    
    // MARK: - MouseMotionListener

    @Override
    public void mouseMoved(MouseEvent e) {
        Component source = e.getComponent();
        Point location = e.getPoint();
        direction = 0;

        if (location.x < dragInsets.left) {
            direction += WEST;
        }

        if (location.x > source.getWidth() - dragInsets.right - 1) {
            direction += EAST;
        }

        if (location.y < dragInsets.top) {
            direction += NORTH;
        }

        if (location.y > source.getHeight() - dragInsets.bottom - 1) {
            direction += SOUTH;
        }

        //  Mouse is no longer over a resizable border
        if (direction == 0) {
            source.setCursor(sourceCursor);
        } else // use the appropriate resizable cursor
        {
            int cursorType = cursors.get(direction);
            Cursor cursor = Cursor.getPredefinedCursor(cursorType);
            source.setCursor(cursor);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (state == State.inactive) {
            Component source = e.getComponent();
            sourceCursor = source.getCursor();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (state == State.inactive) {
            Component source = e.getComponent();
            source.setCursor(sourceCursor);
        }
    }
    
    // MARK: - MouseListener

    @Override
    public void mousePressed(MouseEvent e) {
        //  Setup for resizing. All future dragging calculations are done based
        //  on the original bounds of the component and mouse pressed location.
        if(e.getPoint().y > dragInsets.top && e.getPoint().y <= dragInsets.top + moveInset) {
            state = State.moving;
        } else {
            state = State.resizing;
        }
        
        // The mouseMoved event continually updates this variable
        if (direction == 0 && state != State.moving) {
            return;
        }

        Component source = e.getComponent();
        pressed = e.getPoint();
        SwingUtilities.convertPointToScreen(pressed, source);
        bounds = source.getBounds();

        //  Making sure autoscrolls is false will allow for smoother resizing
        //  of components
        if (source instanceof JComponent) {
            JComponent jc = (JComponent) source;
            autoscrolls = jc.getAutoscrolls();
            jc.setAutoscrolls(false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        state = State.inactive;

        Component source = e.getComponent();
        source.setCursor(sourceCursor);

        if (source instanceof JComponent) {
            ((JComponent) source).setAutoscrolls(autoscrolls);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (state == State.inactive) {
            return;
        }

        Component source = e.getComponent();
        Point dragged = e.getPoint();
        SwingUtilities.convertPointToScreen(dragged, source);
        
        switch(state) {
            case resizing:
                changeBounds(source, direction, bounds, pressed, dragged);
                break;
            case moving:
                move(source, pressed, dragged);
                pressed = dragged;
                break;
        }

    }
    
    // MARK: - Private properties

    private void changeBounds(Component source, int direction, Rectangle bounds, Point pressed, Point current) {
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;

        //  Resizing the West or North border affects the size and location
        if (WEST == (direction & WEST)) {
            int drag = pressed.x - current.x;
            x -= drag;
            width += drag;
        }

        if (NORTH == (direction & NORTH)) {
            int drag = pressed.y - current.y;
            y -= drag;
            height += drag;
        }

        //  Resizing the East or South border only affects the size
        if (EAST == (direction & EAST)) {
            int drag = current.x - pressed.x;
            width += drag;
        }

        if (SOUTH == (direction & SOUTH)) {
            int drag = current.y - pressed.y;
            height += drag;
        }

        if(width < source.getMinimumSize().width || height < source.getMinimumSize().height) {
            return;
        }
        
        source.setBounds(x, y, width, height);
        source.validate();
    }
    
    private void move(Component source, Point oldPoint, Point newPoint) {
        int dx = newPoint.x - oldPoint.x;
        int dy = newPoint.y - oldPoint.y;
        
        Point sourceLocation = source.getLocation();
        int newX = sourceLocation.x + dx;
        int newY = sourceLocation.y + dy;
        
        source.setLocation(new Point(newX, newY));
    }
    
}
