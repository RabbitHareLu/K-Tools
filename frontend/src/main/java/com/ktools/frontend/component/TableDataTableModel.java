package com.ktools.frontend.component;

import com.ktools.frontend.Main;
import com.ktools.frontend.common.utils.DialogUtil;
import com.ktools.frontend.panel.TableDataPanel;
import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.api.DataSourceApi;
import com.ktools.warehouse.common.utils.CollectionUtil;
import com.ktools.warehouse.manager.datasource.jdbc.model.TableColumn;
import com.ktools.warehouse.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.warehouse.manager.datasource.jdbc.query.QueryCondition;
import com.ktools.warehouse.mybatis.entity.TreeEntity;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;

import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 18:11
 */
public class TableDataTableModel extends AbstractTableModel {

    private List<Object[]> data = new ArrayList<>();
    private List<String> columnNames = new ArrayList<>();

    public TableDataTableModel() {
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

    public void nextPage(TableDataPanel tableDataPanel, long pageIndex, long pageSize, String tableName) {
        data = new ArrayList<>(Integer.parseInt(String.valueOf(pageSize)));
        loadData(tableDataPanel, pageIndex, pageSize);
    }

    public void loadData(TableDataPanel tableDataPanel, long pageIndex, long pageSize) {
        Tree instance = Tree.getInstance();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(tableDataPanel.getSelectionPath());

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
                columnNames = new ArrayList<>(columns.size() + 1);
                columnNames.add("");

                for (Map.Entry<String, TableColumn> stringTableColumnEntry : columns.entrySet()) {
                    columnNames.add(stringTableColumnEntry.getKey());
                }

                QueryCondition queryCondition = new QueryCondition();
                queryCondition.setPageNum(pageIndex);
                queryCondition.setPageSize(pageSize);
                queryCondition.setTotal(pageIndex * pageSize);

                Page<Row> rowPage = KToolsContext.getInstance()
                        .getApi(DataSourceApi.class)
                        .selectData(String.valueOf(jdbcTreeEntity.getId()),
                                schemaTreeEntity.getNodeName(),
                                treeEntity.getNodeName(),
                                queryCondition
                        );

                long totalRow = rowPage.getTotalRow();

                long count = 0;
                if (pageIndex <= 1) {
                    count = 1;
                } else {
                    count = (pageIndex - 1) * pageSize + 1;
                }

                for (Row record : rowPage.getRecords()) {
                    Object[] valueArr = new Object[columns.size() + 1];
                    valueArr[0] = count;
                    int index = 1;
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
                    data.add(valueArr);
                    count++;
                }

                if (data.size() < pageSize) {
                    tableDataPanel.getTotalDataCountButton().setText(String.valueOf((pageIndex - 1) * pageSize + data.size()));
                    tableDataPanel.getNextPageButton().setEnabled(false);
                } else {
                    tableDataPanel.getTotalDataCountButton().setText(totalRow + "+");
                }
                tableDataPanel.getCurrentPageButton().setText((pageIndex - 1) * pageSize + 1 + "-" + (count - 1));
            }
        } catch (Exception e) {
            DialogUtil.showErrorDialog(Main.kToolsRootJFrame, e.getMessage());
            throw new RuntimeException(e);
        }

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

}
