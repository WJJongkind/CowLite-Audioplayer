package cap.gui;

import cap.core.audio.AudioController;
import cap.util.IO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.plaf.metal.MetalSliderUI;

/**
 * (c) Copyright
 * To make a better looking timebar
 */
public class TimeSlider extends MetalSliderUI
{
    private final float[] fractions = {0.0f, 0.5f};
    private Color[] fillColors = {
      new Color(0x0000ff),
      new Color(0x1658AE)
    };
    private Color[] backColors = {
      new Color(0x7C818D),
      new Color(0x575C68)
    };
    private Paint hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private Paint hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private Paint vFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private Paint vBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private final Stroke roundEndStroke = new BasicStroke(8,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    private Color timecolor = new Color(0x00e600);
    
    private final AudioController AUDIO;
    
    public TimeSlider(Map<String, Object> graphics, AudioController controller) 
    {
        //super(b);
        this.AUDIO = controller;
        try{
            setPrimaryBack((Color) graphics.get("sliderbackprim"));
            setSecondaryBack((Color) graphics.get("sliderbacksec"));
            
            setPrimary((Color) graphics.get("timeprim"));
            setSecondary((Color) graphics.get("timesec"));
            timecolor = (Color) graphics.get("time");
        }catch(Exception e){System.out.println("TimeSliderConstructor" + e);}
    }
    
    @Override
    public void paintThumb(Graphics g)
    {
    }
    
    private String seconds(int duration)
    {
        String str;
        int seconds = (int)(duration%60);
        if(seconds < 10)
        {
            str = "0"+seconds;
        }
        else
            str = seconds + "";
        return str;
    }
    
    private String minutes(int duration)
    {
        String str;
        int minutes = (int)(duration/60);
        if(minutes < 10)
        {
            str = "0"+minutes;
        }
        else
            str = minutes + "";
        return str;
    }
    
   @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int cy = (trackRect.height / 2) - 2;
            g.translate(trackRect.x, trackRect.y + cy);

            g2.setStroke(roundEndStroke);
            g2.setPaint(hBackGradient);
            g2.drawLine(thumbRect.x, 2, trackRect.width, 2);
            g2.setPaint(hFillGradient);
            g2.drawLine(0, 2, thumbRect.x, 2);

            g.translate(-trackRect.x, -(trackRect.y + cy));
        } else {
            int cx = (trackRect.width / 2) - 2;
            g.translate(trackRect.x + cx, trackRect.y);

            g2.setStroke(roundEndStroke);
            g2.setPaint(vBackGradient);
            g2.drawLine(2, 0, 2, thumbRect.y);
            g2.setPaint(vFillGradient);
            g2.drawLine(2, thumbRect.y, 2, trackRect.height);

            g.translate(-(trackRect.x + cx), -trackRect.y);
        }
  }
  
  /**
   * sets the primary color of this slider
   * @param c color
   */
  public void setPrimary(Color c)
  {
      fillColors[0] = c;
      hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  /**
   * sets the primary background color of this slider
   * @param c color
   */
  public void setPrimaryBack(Color c)
  {
      backColors[0] = c;
      hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
    /**
   * sets the secondary background color of this slider
   * @param c color
   */
  public void setSecondaryBack(Color c)
  {
      backColors[1] = c;
      hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
   /**
   * sets the secondary color of this slider
   * @param c color
   */
  public void setSecondary(Color c)
  {
      fillColors[1] = c;
      hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  /**
   * set the color of the timer
   * @param c  color
   */
  public void setTimeColor(Color c)
  {
      timecolor = c;
  }

    public Color[] getFillColors() {
        return fillColors;
    }

    public Color[] getBackColors() {
        return backColors;
    }

    public Color getTimecolor() {
        return timecolor;
    }
}
    

