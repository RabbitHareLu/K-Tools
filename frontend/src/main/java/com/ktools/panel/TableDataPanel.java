package com.ktools.panel;

import com.ktools.KToolsContext;
import com.ktools.Main;
import com.ktools.api.DataSourceApi;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.common.utils.DialogUtil;
import com.ktools.component.TableDataTableModel;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.manager.datasource.jdbc.model.TableColumn;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.manager.datasource.jdbc.query.QueryCondition;
import com.ktools.mybatis.entity.TreeEntity;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 17:11
 */
@Slf4j
@Data
public class TableDataPanel extends JPanel {

    private TreePath selectionPath;
    private JTable jTable;

    public TableDataPanel() {
    }

    public TableDataPanel(TreePath selectionPath) {
        this.selectionPath = selectionPath;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        Box rootBox = Box.createVerticalBox();
        add(rootBox);

//        JToolBar jToolBar = new JToolBar();
//        jToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
//        jToolBar.setFloatable(false);
//        jToolBar.setBorder(null);
//        JButton refreshButton = new JButton(ImageLoad.getInstance().getTableRefreshIcon());
//        refreshButton.setFocusable(false);
//        jToolBar.add(refreshButton);
//
//        Box conditionBox = Box.createHorizontalBox();
//        JLabel where = new JLabel("WHERE");
//        conditionBox.add(where);
//
//        rootBox.add(jToolBar);
//        rootBox.add(conditionBox);

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
                initTableModel();

                autoAdjustColumnWidth(jTable);
            }
        }.execute();


    }

    private void initTableModel() {
        TableDataTableModel tableDataTableModel = (TableDataTableModel) jTable.getModel();

        Tree instance = Tree.getInstance();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);

        TreeNode schemaNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent()));
        TreeEntity schemaTreeEntity = schemaNode.getTreeEntity();

        TreeNode jdbcNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent().getParent()));
        TreeEntity jdbcTreeEntity = jdbcNode.getTreeEntity();
        TreeEntity treeEntity = currentTreeNode.getTreeEntity();

        try {
            // 建立jdbc连接
            KToolsContext.getInstance()
                    .getApi(DataSourceApi.class)
                    .conn(String.valueOf(jdbcTreeEntity.getId()),
                            jdbcTreeEntity.getNodeType(),
                            jdbcTreeEntity.getNodeInfo());

            // 查询表元数据
            TableMetadata tableMetadata = KToolsContext.getInstance()
                    .getApi(DataSourceApi.class)
                    .selectTableMetadata(String.valueOf(jdbcTreeEntity.getId()),
                            schemaTreeEntity.getNodeName(),
                            treeEntity.getNodeName());

            Map<String, TableColumn> columns = tableMetadata.getColumns();

            if (CollectionUtil.isNotEmpty(columns)) {
                for (Map.Entry<String, TableColumn> stringTableColumnEntry : columns.entrySet()) {
                    tableDataTableModel.addColumn(stringTableColumnEntry.getKey());
                }

                QueryCondition queryCondition = new QueryCondition();
                queryCondition.setPageNum(1L);
                queryCondition.setPageSize(200L);

                Page<Row> rowPage = KToolsContext.getInstance()
                        .getApi(DataSourceApi.class)
                        .selectData(String.valueOf(jdbcTreeEntity.getId()),
                                schemaTreeEntity.getNodeName(),
                                treeEntity.getNodeName(),
                                queryCondition
                        );


                for (Row record : rowPage.getRecords()) {
                    Object[] valueArr = new Object[columns.size()];
                    int index = 0;
                    for (Map.Entry<String, TableColumn> stringTableColumnEntry : columns.entrySet()) {
                        TableColumn value = stringTableColumnEntry.getValue();
                        switch (value.getDataType().getName()) {
                            case "DECIMAL" -> {
                                BigDecimal bigDecimal = record.getBigDecimal(stringTableColumnEntry.getKey());
                                if (Objects.nonNull(bigDecimal)) {
                                    valueArr[index] = bigDecimal.toPlainString();
                                } else {
                                    valueArr[index] = null;
                                }
                            }

                            default -> valueArr[index] = record.get(stringTableColumnEntry.getKey());
                        }

                        index++;
                    }
                    tableDataTableModel.addRow(valueArr);
                }
            }


        } catch (Exception e) {
            DialogUtil.showErrorDialog(Main.kToolsRootJFrame, e.getMessage());
            throw new RuntimeException(e);
        }

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
