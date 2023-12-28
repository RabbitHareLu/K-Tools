package com.ktools.frontend.panel;

import com.ktools.frontend.component.ImageLoad;
import com.ktools.frontend.component.TableDataTableModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 17:11
 */
@Slf4j
@Data
public class TableDataPanel extends JPanel {

    private TreePath selectionPath;
    private String tableName;
    private JTable jTable;

    private JButton currentPageButton;
    private JButton totalDataCountButton;
    private JButton nextPageButton;
    private JButton previousPageButton;

    private long pageIndex = 1;
    private long pageSize = 100;

    public TableDataPanel() {
    }

    public TableDataPanel(TreePath selectionPath) {
        this.selectionPath = selectionPath;
        setLayout(new BorderLayout());
        init();
    }

    public TableDataPanel(TreePath selectionPath, String tableName) {
        this.selectionPath = selectionPath;
        this.tableName = tableName;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        Box rootBox = Box.createVerticalBox();
        add(rootBox);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));

        previousPageButton = new JButton(ImageLoad.getInstance().getPreviousPageIcon());

        Dimension buttonDimension = new Dimension(24, previousPageButton.getPreferredSize().height);

        previousPageButton.setBorder(null);
        previousPageButton.setMinimumSize(buttonDimension);
        previousPageButton.setPreferredSize(buttonDimension);
        previousPageButton.setMaximumSize(buttonDimension);
        previousPageButton.setEnabled(false);
        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageIndex--;
                TableDataTableModel tableDataTableModel = (TableDataTableModel) jTable.getModel();
                tableDataTableModel.nextPage(TableDataPanel.this, pageIndex, pageSize, tableName);
                tableDataTableModel.fireTableDataChanged();
                if (pageIndex <= 1) {
                    previousPageButton.setEnabled(false);
                }
                nextPageButton.setEnabled(true);
            }
        });


        buttonBox.add(previousPageButton);
        buttonBox.add(Box.createRigidArea(new Dimension(5, 0)));

        currentPageButton = new JButton("1-100");

        buttonBox.add(currentPageButton);
        buttonBox.add(Box.createRigidArea(new Dimension(5, 0)));

        totalDataCountButton = new JButton("100");

        buttonBox.add(totalDataCountButton);
        buttonBox.add(Box.createRigidArea(new Dimension(5, 0)));

        nextPageButton = new JButton(ImageLoad.getInstance().getNextPageIcon());
        nextPageButton.setBorder(null);
        nextPageButton.setMinimumSize(buttonDimension);
        nextPageButton.setPreferredSize(buttonDimension);
        nextPageButton.setMaximumSize(buttonDimension);
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageIndex++;
                TableDataTableModel tableDataTableModel = (TableDataTableModel) jTable.getModel();
                tableDataTableModel.nextPage(TableDataPanel.this, pageIndex, pageSize, tableName);
                tableDataTableModel.fireTableDataChanged();
                previousPageButton.setEnabled(true);
            }
        });

        buttonBox.add(nextPageButton);

        buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton refreshButton = new JButton(ImageLoad.getInstance().getTableRefreshIcon());
        refreshButton.setBorder(null);
        refreshButton.setMinimumSize(buttonDimension);
        refreshButton.setPreferredSize(buttonDimension);
        refreshButton.setMaximumSize(buttonDimension);

        buttonBox.add(refreshButton);

        buttonBox.add(Box.createHorizontalGlue());

        rootBox.add(buttonBox);

        TableDataTableModel tableDataTableModel = new TableDataTableModel();
        jTable = new JTable(tableDataTableModel);
        jTable.setAutoCreateRowSorter(true);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.setShowHorizontalLines(true);
        jTable.setShowVerticalLines(true);

        JScrollPane scrollPane = new JScrollPane(jTable);
        rootBox.add(scrollPane);
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                return null;
            }

            @Override
            protected void done() {
                tableDataTableModel.loadData(TableDataPanel.this, pageIndex, pageSize);
                tableDataTableModel.fireTableStructureChanged();
                autoAdjustColumnWidth(jTable);
            }
        }.execute();


    }

    private void autoAdjustColumnWidth(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();

        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxWidth = 0;

            // Find the maximum width of the content in the column
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Object value = table.getValueAt(row, column);
                Component cellComponent = cellRenderer.getTableCellRendererComponent(table, value, false, false, row, column);
                maxWidth = Math.max(maxWidth, cellComponent.getPreferredSize().width);
            }

            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = table.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
            int headerWidth = headerComponent.getPreferredSize().width;

            // Set the preferred width of the column
            javax.swing.table.TableColumn tableColumn = columnModel.getColumn(column);
            tableColumn.setPreferredWidth(Math.max(maxWidth, headerWidth) + 20); // Add some padding
        }
    }
}
