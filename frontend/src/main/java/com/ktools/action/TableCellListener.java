package com.ktools.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年06月30日 19:17
 */
public class TableCellListener implements PropertyChangeListener, Runnable {
    private JTable table;
    private Action action;
    private int row;
    private int column;
    private Object oldValue;
    private Object newValue;

    public TableCellListener(JTable table, Action action) {
        this.table = table;
        this.action = action;
        this.table.addPropertyChangeListener(this);
    }

    private TableCellListener(JTable table, int row, int column, Object oldValue, Object newValue) {
        this.table = table;
        this.row = row;
        this.column = column;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public int getColumn() {
        return column;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public int getRow() {
        return row;
    }

    public JTable getTable() {
        return table;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // A cell has started/stopped editing
        if ("tableCellEditor".equals(evt.getPropertyName())) {
            if (table.isEditing()) {
                //System.out.printf("tableCellEditor is editing..%n");
                processEditingStarted();
            } else {
                //System.out.printf("tableCellEditor editing stopped..%n");
                processEditingStopped();
            }
        }
    }

    private void processEditingStarted() {
        // The invokeLater is necessary because the editing row and editing
        // column of the table have not been set when the "tableCellEditor"
        // PropertyChangeEvent is fired.
        // This results in the "run" method being invoked
        SwingUtilities.invokeLater(this);
    }

    private void processEditingStopped() {
        newValue = table.getModel().getValueAt(row, column);
        //这里应对newValue为null的情况做处理，否则后面会抛出异常
        if (newValue == null)
            newValue = "";
        // The data has changed, invoke the supplied Action
        if (!newValue.equals(oldValue)) {
            // Make a copy of the data in case another cell starts editing
            // while processing this change
            TableCellListener tcl = new TableCellListener(
                    getTable(), getRow(), getColumn(), getOldValue(), getNewValue());
            ActionEvent event = new ActionEvent(
                    tcl,
                    ActionEvent.ACTION_PERFORMED,
                    "");
            action.actionPerformed(event);
        }
    }

    @Override
    public void run() {
        row = table.convertRowIndexToModel(table.getEditingRow());
        column = table.convertColumnIndexToModel(table.getEditingColumn());
        oldValue = table.getModel().getValueAt(row, column);
        //这里应对oldValue为null的情况做处理，否则将导致原值与新值均为空时仍被视为值改变
        if (oldValue == null)
            oldValue = "";
        newValue = null;
    }
}
