/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.settings.layout;

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
        switch(orientation) {
            case horizontal:
                this.position = clamp(mouseLocation.x / ((double) getWidth()), 0, 1);
                break;
            case vertical:
                this.position = clamp(mouseLocation.y / ((double) getHeight()), 0, 1);
                break;
        }
        
        repaint();
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, orientation == Orientation.horizontal ? colors.get(mouseLocation.x) : colors.get(mouseLocation.y)));
    }
    
    private void calculateColorsIfNeeded() {
        if(colors.isEmpty() || !getSize().equals(previousSize)) {
            colors.clear();
            previousSize = getSize();
            
            int size = orientation == Orientation.horizontal ? getWidth() : getHeight();
            
            for(int i = 0; i < size; i++) {
                colors.add(getColorForRelativePosition(i / ((double) size)));
            }
        }
    }
    
    private Color getColorForRelativePosition(double position) {
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
    
}
