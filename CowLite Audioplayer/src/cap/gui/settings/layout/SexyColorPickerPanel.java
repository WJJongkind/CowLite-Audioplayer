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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Wessel
 */
public class SexyColorPickerPanel extends JComponent implements MouseListener, MouseMotionListener {
    
    // MARK: - Associated types & constants
    
    public interface Delegate {
        public void didSelectColor(SexyColorPickerPanel sender, Color color);
    }
    
    private static final class Layout {
        public static final int knobColorDiameter = 19;
        public static final int borderDiameter = 3;
    }
    
    // MARK: - Private properties
    
    private final Point.Double position;
    private final ArrayList<Gradient> colorMap = new ArrayList<>();
    
    private Color baseColor;
    private Dimension previousSize;
    private WeakReference<Delegate> delegate;
    
    // MARK: - Initialisers
    
    public SexyColorPickerPanel(Color initialBaseColor) {
        this.baseColor = initialBaseColor;
        this.position = new Point.Double(1, 0);
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
    }
    
    // MARK: - JComponent
    
    @Override
    public void paintComponent(Graphics g) {
        calculateGradientsIfNeeded();
        
        Graphics2D graphics2D = (Graphics2D) g;
        
        // Drawing the main panel with gradients
        for(int y = 0; y < colorMap.size(); y++) {
            Gradient gradient = colorMap.get(y);
            GradientPaint paint = new GradientPaint(0, y, gradient.startColor, getWidth(), y, gradient.endColor);
            graphics2D.setPaint(paint);
            g.drawLine(0, y, getWidth(), y);
        }
        
        // Knob
        drawKnob(graphics2D);
        
    }
    
    private void drawKnob(Graphics2D graphics2D) {
        // Drawing positions
        int x = (int) Math.round(position.x * getWidth());
        int y = (int) Math.round(position.y * getHeight());
        int knobStartX = x - Layout.knobColorDiameter / 2;
        int knobStartY = y - Layout.knobColorDiameter / 2;
        
        int whiteBorderSize = Layout.knobColorDiameter + 2 * Layout.borderDiameter;
        int whiteBorderX = x - whiteBorderSize / 2;
        int whiteBorderY = y - whiteBorderSize / 2;
        
        // Anti-aliasing
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // White border
        graphics2D.setColor(Color.white);
        graphics2D.fillOval(whiteBorderX, whiteBorderY, whiteBorderSize, whiteBorderSize);
        
        // Dot
        graphics2D.setColor(calculateColorForCurrentPosition());
        graphics2D.fillOval(knobStartX, knobStartY, Layout.knobColorDiameter, Layout.knobColorDiameter);
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public void setBaseColor(Color color) throws IllegalArgumentException {
        this.baseColor = color;
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
        position.x = clamp(((double) mouseLocation.x) / getWidth(), 0, 1);
        position.y = clamp(((double) mouseLocation.y) / getHeight(), 0, 1);
        
        repaint();
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, calculateColorForCurrentPosition()));
    }
    
    private Color calculateColorForCurrentPosition() {
        int r = calculateColorComponentForCurrentPosition(baseColor.getRed());
        int g = calculateColorComponentForCurrentPosition(baseColor.getGreen());
        int b = calculateColorComponentForCurrentPosition(baseColor.getBlue());
        
        return new Color(r, g, b);
    }
    
    private int calculateColorComponentForCurrentPosition(int baseValue) {
        return (int) Math.round((baseValue + ((255 - baseValue) * (1 - position.x))) * (1 - position.y));
    }
    
    private void calculateGradientsIfNeeded() {
        if(!getSize().equals(previousSize) || colorMap.isEmpty()) {
            colorMap.clear();
            for(int y = 0; y < getHeight(); y++) {
                double proportionalHeight = y / ((double)getHeight());

                int startColorComponents = (int) Math.round((1 - proportionalHeight) * 255);
                Color startColor = new Color(startColorComponents, startColorComponents, startColorComponents);

                int endR = (int) Math.round((1 - proportionalHeight) * baseColor.getRed());
                int endG = (int) Math.round((1 - proportionalHeight) * baseColor.getGreen());
                int endB = (int) Math.round((1 - proportionalHeight) * baseColor.getBlue());
                Color endColor = new Color(endR, endG, endB);

                colorMap.add(new Gradient(startColor, endColor));
            }
        }
    }
    
    // MARK: - Private associated types
    
    private class Gradient {
        public Color startColor;
        public Color endColor;
        
        public Gradient(Color startColor, Color endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }
        
    }
    
}
