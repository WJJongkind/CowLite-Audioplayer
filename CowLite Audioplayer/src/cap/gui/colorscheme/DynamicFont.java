/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Font;

/**
 *
 * @author Wessel
 */
public final class DynamicFont {
    
    // MARK: - Associated types & constants
    
    private static final class Constants {
        
        // MARK: - Size scaling factor
        
        public static final double scaleFactor = 0.25;
        
        // MARK: - Default sizes
        
        public static final float xs = 8;
        public static final float s = 11;
        public static final float m = 15;
        public static final float l = 19;
        public static final float xl = 24;
        public static final float xxl = 30;
        
    }
    
    // MARK: - Private properties
    
    private final Font font;
    private int scale = 0;
    
    // MARK: - Initialisers
    
    public DynamicFont(Font font) {
        this.font = font.deriveFont(Font.PLAIN);
    }
    
    private DynamicFont(Font font, float size) {
        this.font = font.deriveFont(size);
    }
    
    private DynamicFont(Font font, int style) {
        this.font = font.deriveFont(style);
    }
    
    // MARK: - Public methods
    
    public Font get() {
        float fontSize = (float) (font.getSize() + scale * Constants.scaleFactor * font.getSize());
        return font.deriveFont(fontSize);
    }
    
    public DynamicFont plain() {
        return new DynamicFont(font, Font.PLAIN);
    }
    
    public DynamicFont bold() {
        return new DynamicFont(font, font.getStyle() + Font.BOLD);
    }
    public DynamicFont italic() {
        return new DynamicFont(font, font.getStyle() + Font.ITALIC);
    }
    
    public DynamicFont xs() {
        return new DynamicFont(font, Constants.xs);
    }
    
    public DynamicFont s() {
        return new DynamicFont(font, Constants.s);
    }
    
    public DynamicFont m() {
        return new DynamicFont(font, Constants.m);
    }
    
    public DynamicFont l() {
        return new DynamicFont(font, Constants.l);
    }
    
    public DynamicFont xl() {
        return new DynamicFont(font, Constants.xl);
    }
    
    public DynamicFont xxl() {
        return new DynamicFont(font, Constants.xxl);
    }
    
    public void setScale(int scale) {
        this.scale = scale;
    }
    
}
