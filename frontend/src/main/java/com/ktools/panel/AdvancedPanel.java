package com.ktools.panel;

import com.ktools.action.TableCellListener;
import com.ktools.common.utils.BoxUtil;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.component.KTTableModel;
import com.ktools.manager.datasource.model.KDataSourceConfig;
import com.ktools.manager.datasource.model.KDataSourceMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 17:01
 */
@Data
@Slf4j
public class AdvancedPanel extends JPanel {

    private Map<String, String> advanceValueMap = new LinkedHashMap<>();
    private Map<String, String> keyNameMap;
    private KDataSourceMetadata kDataSourceMetadata;
    private JTable jTable;

    public AdvancedPanel() {
        setLayout(new BorderLayout());

    }

    public AdvancedPanel(KDataSourceMetadata kDataSourceMetadata) {
        this.kDataSourceMetadata = kDataSourceMetadata;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        initTableUI();
        List<KDataSourceConfig> config = kDataSourceMetadata.getConfig();

        if (CollectionUtil.isNotEmpty(config)) {
            keyNameMap = config.stream().collect(Collectors.toMap(KDataSourceConfig::getName, KDataSourceConfig::getKey));
            KTTableModel model = (KTTableModel) jTable.getModel();
            for (KDataSourceConfig kDataSourceConfig : config) {
                if (Objects.equals(kDataSourceConfig.getKey(), "jdbcUrl") ||
                        Objects.equals(kDataSourceConfig.getKey(), "username") ||
                        Objects.equals(kDataSourceConfig.getKey(), "password")) {
                    continue;
                }
                String[] rowData = new String[2];
                rowData[0] = kDataSourceConfig.getName();
                rowData[1] = kDataSourceConfig.getDefaultValue();
                model.addRow(rowData);
            }
        }

    }

    public void collectValue() {
        if (jTable.isEditing()) {
            jTable.getCellEditor().stopCellEditing();
        }
        KTTableModel model = (KTTableModel) jTable.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            String[] rowData = new String[2];
            for (int col = 0; col < 2; col++) {
                rowData[col] = String.valueOf(model.getValueAt(row, col));
            }
            keyNameMap.computeIfPresent(rowData[0], (k, v) -> advanceValueMap.put(v, rowData[1]));
        }
    }

    private void initTableUI() {
        Box box = Box.createVerticalBox();
        add(box, BorderLayout.CENTER);

        // 创建表格模型
        KTTableModel model = new KTTableModel();
        jTable = new JTable(model);
        jTable.setAutoCreateRowSorter(true);
        model.addColumn("名称");
        model.addColumn("值");

        // 创建滚动窗格以支持大量数据
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jTable.isEditing()) {
                    jTable.getCellEditor().stopCellEditing();
                }
            }
        });

        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                log.info("cell changed");
                log.info("Row: {}, Column: {}", tcl.getRow(), tcl.getColumn());
                log.info("old value: {}, new value: {}", tcl.getOldValue(), tcl.getNewValue());
            }
        };

        new TableCellListener(jTable, action);

        // 将表格添加到窗口
        box.add(scrollPane);
        BoxUtil.addVerticalStrut(box, 30);
    }


}
