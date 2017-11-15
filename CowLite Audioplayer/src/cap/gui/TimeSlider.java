package cap.gui;

import cap.core.CowLiteAudioPlayer;
import static cap.core.CowLiteAudioPlayer.colorindex;
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
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.plaf.metal.MetalSliderUI;

/**
 * (c) Copyright
 * To make a better looking timebar
 */
public class TimeSlider extends MetalSliderUI
{
    private Image knobImage;
    private static final float[] fractions = {0.0f, 0.5f};
    public static Color[] fillColors = {
      new Color(0x0000ff),
      new Color(0x1658AE)
    };
    public static Color[] backColors = {
      new Color(0x7C818D),
      new Color(0x575C68)
    };
    private static  Paint hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Paint hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Paint vFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Paint vBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static final Stroke roundEndStroke = new BasicStroke(8,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    public static Color timecolor = new Color(0x00e600);
    public TimeSlider() 
    {
        //super(b);
        try{
            knobImage = ImageIO.read(new File(CowLiteAudioPlayer.docPath + "CowLite Audio Player\\resources\\graphics\\timeknob.png"));
            setPrimaryBack(colorindex.get(4));
            setSecondaryBack(colorindex.get(5));
            
            setPrimary(colorindex.get(8));
            setSecondary(colorindex.get(9));
            timecolor = colorindex.get(10);
        }catch(Exception e){System.out.println("TimeSliderConstructor" + e);}
    }
    
    @Override
    public void paintThumb(Graphics g)
    {
        g.drawImage(this.knobImage, thumbRect.x, thumbRect.y - 4, 25,25,null);
        try{
        g.setColor(timecolor);
        g.drawString(GraphicalInterface.time, trackRect.width / 2, trackRect.height - 3);}catch(Exception e){}
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
  public static void setPrimary(Color c)
  {
      fillColors[0] = c;
      hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  /**
   * sets the primary background color of this slider
   * @param c color
   */
  public static void setPrimaryBack(Color c)
  {
      backColors[0] = c;
      hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
    /**
   * sets the secondary background color of this slider
   * @param c color
   */
  public static void setSecondaryBack(Color c)
  {
      backColors[1] = c;
      hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
   /**
   * sets the secondary color of this slider
   * @param c color
   */
  public static void setSecondary(Color c)
  {
      fillColors[1] = c;
      hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  /**
   * set the color of the timer
   * @param c  color
   */
  public static void setTimeColor(Color c)
  {
      timecolor = c;
  }
}
    

