/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.DynamicFont;
import cap.gui.colorscheme.TableColorScheme;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.ref.WeakReference;
import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wessel
 */
public class Table extends JTable {
    
    // MARK: - Public associated types
    
    public interface Delegate {
        public void didSelectRow(Table sender, int row);
        public void didMoveRow(Table sender, int from, int to);
    }
    
    public interface RowRenderer {
        public void layoutCell(Component component, boolean isSelected, boolean hasFocus, int row);
    }
    
    // MARK: - Private properties
    
    private final Table self = this; // Required for inner classes...
    private final ReorderableTableModel tableModel;
    
    private WeakReference<Delegate> delegate;
    private boolean muteDelegate = false;
    
    // MARK: - Initialisers

    public Table(TableColorScheme colorScheme, DynamicFont font, int columnCount) {
        tableModel = new ReorderableTableModel();
        tableModel.setColumnCount(columnCount);
        super.setModel(tableModel);
        super.getSelectionModel().addListSelectionListener(event -> rowSelected(event));
        super.setDefaultRenderer(Object.class, new AlternatingRowRenderer(colorScheme));
        MouseHandler mouseHandler = new MouseHandler();
        super.addMouseListener(mouseHandler);
        super.addMouseMotionListener(mouseHandler);
        super.setTransferHandler(null);
        
        super.setBackground(colorScheme.getFirstBackgroundColor());
        super.setForeground(colorScheme.getTextColor());
        super.setTableHeader(null);
        super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        super.getSelectionModel().addListSelectionListener(e -> rowSelected(e));
        super.setShowGrid(false);
        super.setFont(font.get());
        super.setDragEnabled(true);
        super.setDropMode(DropMode.INSERT_ROWS);
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public void addRow(Object[] row) {
        if(row.length != super.getModel().getColumnCount()) {
            throw new IllegalArgumentException("Provided data does not have the correct amount of rows.");
        }
        
        int selectedIndex = Math.max(0, super.getSelectedRow());
        
        ReorderableTableModel model = (ReorderableTableModel) getModel();
        model.addRow(row);
        
        // Don't notify delegate of selection as we are not changing the selected item.
        muteDelegate = true;
        selectRow(selectedIndex);
        muteDelegate = false;
        
        super.revalidate();
        super.repaint();
    }
    
    public void clearRows() {
        ReorderableTableModel model = (ReorderableTableModel) getModel();
        model.setRowCount(0);
        
        super.revalidate();
        super.repaint();
    }
    
    public void selectRow(int row) {
        if(row == -1) {
            clearSelection();
            return;
        }
        
        if(row > getModel().getRowCount()) {
            return;
        }
        
        super.setRowSelectionInterval(row, row);
    }
    
    // MARK: - ListSelectionListener
    
    private void rowSelected(ListSelectionEvent event) {
        getParent().revalidate();
        getParent().repaint();
        
        if(muteDelegate) {
            return;
        }
        
        int row = super.getSelectedRow();
        if(event.getValueIsAdjusting() || row == -1) {
            return;
        }
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectRow(this, row));
    }
    
    // MARK: - Private associated types
    
    private class MouseHandler implements MouseListener, MouseMotionListener {
        
        private Integer start = null;
        private Integer row = null;
        
        @Override
        public void mouseClicked(MouseEvent event) {}

        @Override
        public void mousePressed(MouseEvent event) {
            row = self.rowAtPoint(event.getPoint());
            start = row;
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if(start == null || start == row) {
                return;
            }
            unwrappedPerform(delegate, delegate -> delegate.didMoveRow(self, start, row));
            row = null;
            start = null;
        }

        @Override
        public void mouseEntered(MouseEvent event) {}

        @Override
        public void mouseExited(MouseEvent event) {}

        @Override
        public void mouseDragged(MouseEvent event) {
            int currentRow = self.rowAtPoint(event.getPoint());
            
            if(row == null || currentRow == row) {
                return;
            }
            
            tableModel.moveRow(row, row, currentRow);
            row = currentRow;
            muteDelegate = true;
            self.selectRow(currentRow);
            muteDelegate = false;
            
            self.getParent().revalidate();
            self.getParent().repaint();
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }
        
    }
    
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        
        // MARK: - Private properties
        
        private final TableColorScheme colorScheme;
        
        // MARK: - Initialisers
        
        public AlternatingRowRenderer(TableColorScheme colorScheme) {
            this.colorScheme = colorScheme;
        }
        
        // MARK: - DefaultTableCellRenderer
        
        @Override
        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value, 
                                                       boolean isSelected, 
                                                       boolean hasFocus,
                                                       int row, 
                                                       int column) {
            Component c = super.getTableCellRendererComponent(table, 
                value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            if(isSelected) {
                c.setBackground(colorScheme.getHighlightBackgroundColor());
                c.setForeground(colorScheme.getHighlightTextColor());
            } else {
                c.setForeground(colorScheme.getTextColor());
                c.setBackground(row%2==0 ? colorScheme.getFirstBackgroundColor() : colorScheme.getSecondBackgroundColor());
            }
            return c;
        };
    }
    
    private class ReorderableTableModel extends DefaultTableModel {
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
    }

}
