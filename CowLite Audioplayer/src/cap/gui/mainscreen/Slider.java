package cap.gui.mainscreen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * (c) Copyright To make a better looking timebar
 */
public class Slider extends JComponent implements MouseMotionListener, MouseListener {
    
    // MARK: - Associated types & constants
    
    enum Orientation {
        horizontal,
        vertical
    }

    private double value = 0;
    private double minimumValue = 0;
    private double maximumValue = 0;
    private ChangeListener listener;
    private Orientation orientation;

    public Slider(Orientation orientation) {
        this.orientation = orientation;
        addListeners();
    }

    private void addListeners() {
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        double percentage = value / ((double) (maximumValue - minimumValue));
        g2.setColor(getForeground());
        switch(orientation) {
            case horizontal:
                int fill = (int) Math.round(percentage * getWidth());
                g2.fillRect(0, 0, fill, getHeight());
                break;
            case vertical:
                fill = (int) Math.round(percentage * getHeight());
                g2.fillRect(0, Math.max(0, getHeight() - fill), getWidth(), getHeight());
        }
    }

    public void setEditable(boolean editable) {
        if (!editable) {
            this.removeMouseListener(this);
            this.removeMouseMotionListener(this);
        } else {
            addListeners();
        }
    }
    
    public void setMinimumValue(double value) {
        this.minimumValue = value;
        this.maximumValue = Math.max(value, this.maximumValue);
    }
    
    public void setMaximumValue(double value) {
        this.maximumValue = value;
        this.minimumValue = Math.min(value, this.minimumValue);
    }

    public void setValue(double value) {
        this.value = Math.min(maximumValue, Math.max(minimumValue, value));
    }

    private void setValue(MouseEvent e) {
        double percentage = 0;
        switch(orientation) {
            case horizontal:
                percentage = (double) e.getX() / (double) getWidth();
                break;
            case vertical:
                percentage = 1 - ((double) e.getY() / (double) getHeight());
                break;
                
        }
        value = minimumValue + (percentage * (maximumValue - minimumValue));
        repaint();
    }

    public double getValue() {
        return value;
    }

    public void addChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    private void fireStateChanged() {
        if (listener != null) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setValue(e);
        fireStateChanged();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setValue(e);
        fireStateChanged();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
