/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.colorscheme;

import java.awt.Font;

/**
 * This class represents a font that has a "base" size and can be scaled relatively
 * to that base-size. It is used to support accessibility within the application.
 * @author Wessel Jongkind
 */
public final class DynamicFont {
    
    // MARK: - Associated types & constants
    
    private static final class Constants {
        
        // MARK: - Size scaling factor
        
        public static final double scaleFactor = 0.25;
        
        // MARK: - Default sizes
        
        public static final float xs = 10;
        public static final float s = 12;
        public static final float m = 15;
        public static final float l = 19;
        public static final float xl = 24;
        public static final float xxl = 30;
        
    }
    
    // MARK: - Private properties
    
    private final Font font;
    private int scale = 0;
    
    // MARK: - Initialisers
    
    /**
     * 
     * @param font The Font to serve as the base for this DynamicFont.
     */
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
    
    /**
     * 
     * @return The Font representation of this DynamicFont.
     */
    public Font get() {
        float fontSize = (float) getSize();
        return font.deriveFont(fontSize);
    }
    
    /**
     * 
     * @return The size of the font, corrected for the accessibility scale that has been set.
     */
    public int getSize() {
        return (int) Math.round(font.getSize() + scale * Constants.scaleFactor * font.getSize());
    }
    
    /**
     * 
     * @return A plain-text representation of the font.
     */
    public DynamicFont plain() {
        return new DynamicFont(font, Font.PLAIN);
    }
    
    /**
     * 
     * @return A bold-text representation of the font.
     */
    public DynamicFont bold() {
        return new DynamicFont(font, font.getStyle() + Font.BOLD);
    }
    
    /**
     * 
     * @return A italic-text representation of the font.
     */
    public DynamicFont italic() {
        return new DynamicFont(font, font.getStyle() + Font.ITALIC);
    }
    
    /**
     * 
     * @return The extra-small representation of the font (10 points).
     */
    public DynamicFont xs() {
        return new DynamicFont(font, Constants.xs);
    }
    
    /**
     * 
     * @return The small representation of the font (12 points).
     */
    public DynamicFont s() {
        return new DynamicFont(font, Constants.s);
    }
    
    /**
     * 
     * @return The medium representation of the font (15 points).
     */
    public DynamicFont m() {
        return new DynamicFont(font, Constants.m);
    }
    
    /**
     * 
     * @return The large representation of the font (19 points).
     */
    public DynamicFont l() {
        return new DynamicFont(font, Constants.l);
    }
    
    /**
     * 
     * @return The extra-large representation of the font (24 points).
     */
    public DynamicFont xl() {
        return new DynamicFont(font, Constants.xl);
    }
    
    /**
     * 
     * @return The extra-extra-small representation of the font (30 points).
     */
    public DynamicFont xxl() {
        return new DynamicFont(font, Constants.xxl);
    }
    
    /**
     * 
     * @param scale The accessibility scale that needs to be set.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }
    
    // MARK: - Protected methods
    
    /**
     * Makes a shallow copy of the DynamicFont.
     * @return A shallow copy of the DynamicFont.
     */
    protected DynamicFont copy() {
        DynamicFont font = new DynamicFont(this.font);
        font.scale = scale;
        return font;
    }
    
}
