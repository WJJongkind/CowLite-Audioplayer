package cap.gui;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.plaf.metal.MetalSliderUI;
import static cap.core.CowLiteAudioPlayer.colorindex;

/**
 * (c) Copyright
 * To make a better looking volumeslider
 * @author Wessel Jongkind
 */
public class VolumeSlider extends MetalSliderUI
{
    private static final float[] fractions = {0.0f, 0.5f};
    public static Color[] fillColors = {
      new Color(0x00b300),
      new Color(0x00e600)
    };
    public static Color[] backColors = {
      new Color(0x7C818D),
      new Color(0x575C68)
    };
    private static Paint hFillGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Paint hBackGradient = new LinearGradientPaint(0, 0, 0, 11,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Paint vFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Paint vBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    private static Stroke roundEndStroke = new BasicStroke(8,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    
    public VolumeSlider() 
    {
        try
        {
            setPrimaryBack(colorindex.get(4));
            setSecondaryBack(colorindex.get(5));
            
            setPrimary(colorindex.get(6));
            setSecondary(colorindex.get(7));
        }catch(Exception e){}
    }
    
    @Override
    public void paintThumb(Graphics g)
    {
    }
    
 @Override
  public void paintTrack(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      int cx = (trackRect.width / 2) - 2;
      g.translate(trackRect.x + cx, trackRect.y);
 
      g2.setStroke(roundEndStroke);
      g2.setPaint(vBackGradient);
      g2.drawLine(2, 0, 2, thumbRect.y);
      g2.setPaint(vFillGradient);
      g2.drawLine(2, thumbRect.y, 2, trackRect.height);
 
      g.translate(-(trackRect.x + cx), -trackRect.y);
  }

  public static void setPrimary(Color c)
  {
      fillColors[0] = c;
      vFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
     hFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  public static void setSecondary(Color c)
  {
      fillColors[1] = c;
      vFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
      hFillGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, fillColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  public static void setPrimaryBack(Color c)
  {
      backColors[0] = c;
      vBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
      hBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
  
  public static void setSecondaryBack(Color c)
  {
      backColors[1] = c;
      vBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
      hBackGradient = new LinearGradientPaint(0, 0, 11, 0,
            fractions, backColors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
  }
}
    

