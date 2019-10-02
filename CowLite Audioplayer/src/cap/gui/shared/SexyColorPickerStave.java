/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import static cap.util.SugarySyntax.clamp;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 *
 * @author Wessel
 */
public class SexyColorPickerStave extends JComponent implements MouseListener, MouseMotionListener {
    
    // MARK: - Associated types & constants
    
    public enum Orientation {
        horizontal,
        vertical
    }
    
    public interface Delegate {
        public void didSelectColor(SexyColorPickerStave sender, Color color);
    }
    
    private static final class Layout {
        public static final int defaultKnobWidth = 3;
    }
    
    // MARK: - Private properties
    
    private final Orientation orientation;
    private final ArrayList<Color> colors;
    
    private WeakReference<Delegate> delegate;
    private Dimension previousSize;
    private double position;
    private int knobWidth;
    
    // MARK: - Initialisers
    
    public SexyColorPickerStave(Orientation orientation) {
        this.orientation = orientation;
        this.colors = new ArrayList<>();
        this.knobWidth = Layout.defaultKnobWidth;
        this.position = 0.0;
        super.addMouseMotionListener(this);
        super.addMouseListener(this);
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public void setKnobWidth(int width) {
        this.knobWidth = knobWidth;
        repaint();
    }
    
    public Color getColor() {
        int size = orientation == Orientation.horizontal ? getWidth() : getHeight();
        int absolutePosition = clamp((int) Math.round(position * size), 0, colors.size());
        
        // If absolutePosition is not an index of colors, it most likely means that paintComponent has never occured.
        // In that case return red, as that's the default starting color. This seems kinda sketchy so may
        // revisit this at some point in the future.
        return absolutePosition < colors.size() ? colors.get(absolutePosition) : Color.red;
    }
    
    public void setSelectedColor(Color color) {
        position = getRelativePositionForColor(color);
        repaint();
    }
    
    // MARK: - JComponent
    
    @Override
    public void paintComponent(Graphics g) {
        calculateColorsIfNeeded();
        
        int size = orientation == Orientation.horizontal ? getWidth() : getHeight();
        for(int i = 0; i < size; i++) {
            g.setColor(colors.get(i));
            
            switch(orientation) {
                case horizontal:
                    g.drawLine(i, 0, i, getHeight());
                    break;
                case vertical:
                    g.drawLine(0, i, getWidth(), i);
                    break;
            }
        }
        
        Graphics2D graphics2D = (Graphics2D) g;
        g.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(knobWidth));
        switch(orientation) {
            case horizontal:
                int x = (int) Math.round(position * getWidth());
                graphics2D.drawLine(x, 0, x, getHeight());
                break;
            case vertical:
                int y = (int) Math.round(position * getHeight());
                graphics2D.drawLine(0, y, getWidth(), y);
                break;
        }
    }
    
    // MARK: - MouseMotionListener
    
    @Override
    public void mouseDragged(MouseEvent e) {
        handleMouseEvent(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
    
    // MARK: - MouseListener
    
    @Override
    public void mousePressed(MouseEvent e) {
        handleMouseEvent(e.getPoint());
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    // MARK: - Private methods
    
    private void handleMouseEvent(Point mouseLocation) {
        int coordinate = clamp(orientation == Orientation.horizontal ? mouseLocation.x : mouseLocation.y, 0, getLength() - 1);
        
        switch(orientation) {
            case horizontal:
                this.position = clamp(coordinate / ((double) getWidth()), 0, 1);
                break;
            case vertical:
                this.position = clamp(coordinate / ((double) getHeight()), 0, 1);
                break;
        }
        
        repaint();
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, colors.get(coordinate)));
    }
    
    private void calculateColorsIfNeeded() {
        if(colors.isEmpty() || !getSize().equals(previousSize)) {
            colors.clear();
            previousSize = getSize();
            
            int length = getLength();
            for(int i = 0; i < length; i++) {
                colors.add(getColorForRelativePosition(i / ((double) length)));
            }
        }
    }
    
    private int getLength() {
        return orientation == Orientation.horizontal ? getWidth() : getHeight();
    }
    
    private Color getColorForRelativePosition(double position) {
        /*
            The stave is divided into six sections: (rmax, g^), (gmax, rv), (gmax, b^), (bmax, gv), (bmax, r^), (rmax, bv). 
            Each section is of equal size. Below, we first calculate in which segment the position is. Then, we can calculate
            the relative position within that section and thus calculate the color.
        */
        int red = 0;
        int green = 0;
        int blue = 0;
        
        double segmentSize = 1.0 / 6.0;
        if(position > 5.0 / 6.0) {
            double positionInSegment = position - 5 * segmentSize;
            blue = (int) Math.round(255 - (255 * (positionInSegment / segmentSize)));
            red = 255;
        } else if(position > 4.0 / 6.0) {
            double positionInSegment = position - 4 * segmentSize;
            red = (int) Math.round(255 * positionInSegment / segmentSize);
            blue = 255;
        } else if(position > 3.0 / 6.0) {
            double positionInSegment = position - 3 * segmentSize;
            green = (int) Math.round(255 - (255 * (positionInSegment / segmentSize)));
            blue = 255;
        } else if(position > 2.0 / 6.0) {
            double positionInSegment = position - 2 * segmentSize;
            blue = (int) Math.round(255 * positionInSegment / segmentSize);
            green = 255;
        } else if(position > 1.0 / 6.0) {
            double positionInSegment = position - segmentSize;
            red = (int) Math.round(255 - (255 * (positionInSegment / segmentSize)));
            green = 255;
        } else {
            red = 255;
            green = (int) Math.round(255 * position / segmentSize);
        }
        
        return new Color(red, green, blue);
    }
    
    private double getRelativePositionForColor(Color color) {
        // Below are optimized algebraic expressions. They are effectively the inverse of the function getColorForRelativePosition.
        if(color.getRed() == 255) {
            if(color.getGreen() == 0 && color.getBlue() == 0) {
                return 0; // first in segment 1 / last in segment 6
            } else if(color.getGreen() > 0) {
                return color.getGreen() / 1530.0; // segment 1
            } else {
                return color.getBlue() / -1530.0 + 1; // segment 6
            }
        } else if(color.getGreen() == 255) {
            if(color.getRed() == 0 && color.getBlue() == 0) {
                return 1 / 3.0; // Inbetween segment 2 and 3
            } else if(color.getRed() > 0) {
                return color.getRed() / -1530.0 + (1 / 3.0); // segment 2
            } else {
                return color.getBlue() / 1530.0 + (1 / 3.0); // segment 3
            }
        } else {
            if(color.getGreen() == 0 && color.getRed() == 0) {
                return 2 / 3.0;
            } else if(color.getGreen() > 0) {
                return color.getGreen() / -1530.0 + (2 / 3.0); // segment 4
            } else {
                return color.getRed() / 1530.0 + (2 / 3.0); // segment 5
            }
        }
    }
    
}
