package cap.gui.mainscreen;

import cap.gui.colorscheme.ColorScheme;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
* This is an implementation of a JScrollPane with a modern UI
* 
* @author Philipp Danner
*
*/
public class SexyScrollPane extends JScrollPane {

    private static final class Layout {
        private static final int THUMB_SIZE = 6;
        private static final int SB_SIZE = 10;
        private static final int archWidth = 5;
        private static final int archHeight = 12;
    }
    
    private final Color indicatorColor;

    public SexyScrollPane(ColorScheme colorScheme) {
        super.setBorder(null);
        indicatorColor = colorScheme.scrollBar();

        // Set ScrollBar UI
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new ModernScrollBarUI(this));

        horizontalScrollBar.setOpaque(false);
        horizontalScrollBar.setUI(new ModernScrollBarUI(this));

        super.setLayout(new ScrollPaneLayout() {
            private static final long serialVersionUID = 5740408979909014146L;

            @Override
            public void layoutContainer(Container parent) {
                Rectangle availR = ((JScrollPane) parent).getBounds();
                availR.x = availR.y = 0;

                // viewport
                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;
                if (viewport != null) {
                    viewport.setBounds(availR);
                }

                boolean vsbNeeded = isVerticalScrollBarfNecessary();
                boolean hsbNeeded = isHorizontalScrollBarNecessary();

                // vertical scroll bar
                Rectangle vsbR = new Rectangle();
                vsbR.width = Layout.SB_SIZE;
                vsbR.height = availR.height - (hsbNeeded ? vsbR.width : 0);
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;
                if (vsb != null) {
                    vsb.setBounds(vsbR);
                }

                // horizontal scroll bar
                Rectangle hsbR = new Rectangle();
                hsbR.height = Layout.SB_SIZE;
                hsbR.width = availR.width - (vsbNeeded ? hsbR.height : 0);
                hsbR.x = availR.x;
                hsbR.y = availR.y + availR.height - hsbR.height;
                if (hsb != null) {
                    hsb.setBounds(hsbR);
                }
            }
        });

        // Layering
        super.setComponentZOrder(verticalScrollBar, 0);
        super.setComponentZOrder(horizontalScrollBar, 1);
        super.setComponentZOrder(viewport, 2);
    }
    
    private boolean isVerticalScrollBarfNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getHeight() > viewRect.getHeight();
    }

    private boolean isHorizontalScrollBarNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getWidth() > viewRect.getWidth();
    }

    /**
     * Class extending the BasicScrollBarUI and overrides all necessary methods
     */
    private class ModernScrollBarUI extends BasicScrollBarUI {

        private final JScrollPane sp;

        public ModernScrollBarUI(SexyScrollPane sp) {
            this.sp = sp;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            int orientation = scrollbar.getOrientation();
            int x = thumbBounds.x;
            int y = thumbBounds.y;

            int width = orientation == JScrollBar.VERTICAL ? Layout.THUMB_SIZE : thumbBounds.width;
            width = Math.max(width, Layout.THUMB_SIZE);

            int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : Layout.THUMB_SIZE;
            height = Math.max(height, Layout.THUMB_SIZE);

            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(indicatorColor);
            
            if(orientation == JScrollBar.VERTICAL) {
                graphics2D.fill(new RoundRectangle2D.Double(x, y, width, height, Layout.archWidth, Layout.archHeight));
            } else {
                graphics2D.fill(new RoundRectangle2D.Double(x, y, width, height, Layout.archHeight, Layout.archWidth));
            }
            graphics2D.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            sp.repaint();
        }

        /**
         * Invisible Buttons, to hide scroll bar buttons
         */
        private class InvisibleScrollBarButton extends JButton {
            private InvisibleScrollBarButton() {
                super.setOpaque(false);
                super.setFocusable(false);
                super.setFocusPainted(false);
                super.setBorderPainted(false);
                super.setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
    
}