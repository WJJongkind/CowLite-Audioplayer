/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import static cap.util.SugarySyntax.clamp;
import static cap.util.SugarySyntax.maxDouble;
import static cap.util.SugarySyntax.minInt;
import static cap.util.SugarySyntax.sigma;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
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
        graphics2D.setColor(getSelectedColor());
        graphics2D.fillOval(knobStartX, knobStartY, Layout.knobColorDiameter, Layout.knobColorDiameter);
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public Color getBaseColor() {
        return baseColor;
    }
    
    public void setBaseColor(Color color) throws IllegalArgumentException {
        this.baseColor = color;
        repaint();
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, getSelectedColor()));
    }
    
    public Color getSelectedColor() {
        int r = calculateColorComponentForCurrentPosition(baseColor.getRed());
        int g = calculateColorComponentForCurrentPosition(baseColor.getGreen());
        int b = calculateColorComponentForCurrentPosition(baseColor.getBlue());
        
        return new Color(r, g, b);
    }
    
    public void setSelectedColor(Color color) {
        calculateBaseColorAndSetPosition(color);
        repaint();
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, getSelectedColor()));
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
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, getSelectedColor()));
    }
    
    private int calculateColorComponentForCurrentPosition(int baseValue) {
        return (int) Math.round((baseValue + ((255 - baseValue) * (1 - position.x))) * (1 - position.y));
    }
    
    private void calculateBaseColorAndSetPosition(Color color) {
        /*
            We determine the base color of the given color by first translating the
            color to what it would be when y = 0 on the actual color picker panel. This is
            done by determining the brightness of the color. If any of the components
            of the provided color has value 255, then it means the brightness of the
            color is 100%.
        */
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        
        double darkeningFactor = maxDouble(r / 255.0, g / 255.0, b / 255.0);
        position.y = 1 - darkeningFactor;
        
        /*
            We know that at all times, one of the components of a base color has a value of 0.
            Here, we can calculate the color where the darkening has been inversed.
        */
        int brightR = (int) Math.round(r / darkeningFactor);
        int brightG = (int) Math.round(g / darkeningFactor);
        int brightB = (int) Math.round(b / darkeningFactor);
        
        int leastSignificantComponentValue = minInt(brightR, brightG, brightB);
        
        // This is the case when "white" is selected as a color.
        if(sigma(brightR, brightG, brightB) / 3 == leastSignificantComponentValue) {
            position.x = 0;
            baseColor = Color.red;
            return;
        }
        
        /*
            We know the brighteningFactor because we know the value of the component
            with the lowest value. This component will have a value of 0 in the base-color
            state. Knowing this, we can calculate the base color.
        */
        double brighteningFactor = leastSignificantComponentValue / 255.0;
        position.x = 1 - brighteningFactor;
        
        if(brighteningFactor == 0.0) {
            baseColor = new Color(brightR, brightG, brightB);
            return;
        }
        
        int baseR = brightR == 255 ? 255 : (int) Math.round((brightR - brighteningFactor * 255) / (1 - brighteningFactor));
        int baseG = brightG == 255 ? 255 : (int) Math.round((brightG - brighteningFactor * 255) / (1 - brighteningFactor));
        int baseB = brightB == 255 ? 255 : (int) Math.round((brightB - brighteningFactor * 255) / (1 - brighteningFactor));
        
        baseR = clamp(baseR, 0, 255);
        baseG = clamp(baseG, 0, 255);
        baseB = clamp(baseB, 0, 255);
        
        baseColor = new Color(baseR, baseG, baseB);
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
