/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.DynamicFont;
import cap.gui.colorscheme.TableColorScheme;
import static cap.util.SugarySyntax.doTry;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.lang.ref.WeakReference;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
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
    
    private WeakReference<Delegate> delegate;
    
    // MARK: - Initialisers

    public Table(TableColorScheme colorScheme, DynamicFont font, int columnCount) {
        super.setTransferHandler(new TableRowTransferHandler());
        ReorderableTableModel model = new ReorderableTableModel();
        model.setColumnCount(columnCount);
        super.setModel(model);
        super.getSelectionModel().addListSelectionListener(event -> rowSelected(event));
        super.setDefaultRenderer(Object.class, new AlternatingRowRenderer(colorScheme));
        
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
        WeakReference<Delegate> delegateRef = delegate;
        delegate = null;
        selectRow(selectedIndex);
        delegate = delegateRef;
        
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
        super.setRowSelectionInterval(row, row);
    }
    
    // MARK: - ListSelectionListener
    
    private void rowSelected(ListSelectionEvent event) {
        super.revalidate();
        super.repaint();
        
        int row = super.getSelectedRow();
        if(event.getValueIsAdjusting() || row == -1) {
            return;
        }
        
        unwrappedPerform(delegate, delegate -> delegate.didSelectRow(this, row));
    }
    
    // MARK: - Private associated types
    
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
        
        public void reorder(int fromIndex, int toIndex) {
            super.moveRow(fromIndex, fromIndex, toIndex);
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
    }

    private class TableRowTransferHandler extends TransferHandler {
        
        private final DataFlavor localObjectFlavor = new ActivationDataFlavor(Integer.class, "application/x-java-Integer;class=java.lang.Integer", "Integer Row Index");

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new DataHandler(getSelectedRow(), localObjectFlavor.getMimeType());
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport info) {
            boolean b = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
            setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
            return b;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport info) {
            JTable target = (JTable) info.getComponent();
            JTable.DropLocation dropLocation = (JTable.DropLocation) info.getDropLocation();
            int max = getModel().getRowCount();
            int dropIndex = Math.max(0, Math.min(max, dropLocation.getRow()));
            
            target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return doTry(false, () -> {
                Integer fromRow = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
                if (fromRow != -1 && fromRow != dropIndex) {
                    int toRow = fromRow > dropIndex ? dropIndex : dropIndex - 1;
                    ((ReorderableTableModel) getModel()).reorder(fromRow, toRow);
                    unwrappedPerform(delegate, delegate -> delegate.didMoveRow(self, fromRow, toRow));
                    return true;
                }
                
                return false;
            });
        }

        @Override
        protected void exportDone(JComponent c, Transferable t, int act) {
            if ((act == TransferHandler.MOVE) || (act == TransferHandler.NONE)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

}
