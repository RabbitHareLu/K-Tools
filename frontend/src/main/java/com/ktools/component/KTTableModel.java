package com.ktools.component;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月21日 14:01
 */
public class KTTableModel extends AbstractTableModel {

    private List<Object[]> data = new ArrayList<>();
    private List<String> columnNames = new ArrayList<>();

    public KTTableModel() {
    }

    public void addColumn(String columnName) {
        columnNames.add(columnName);
        fireTableStructureChanged();
    }

    public void addRow(Object[] rowData) {
        data.add(rowData);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void deleteRow(int selectedRow) {
        data.remove(selectedRow);
        fireTableRowsDeleted(data.size() - 1, data.size() - 1);
    }


    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

}
