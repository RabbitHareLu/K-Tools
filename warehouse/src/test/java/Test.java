import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.KDataSourceFactory;
import com.ktools.warehouse.manager.datasource.KDataSourceHandler;
import com.ktools.warehouse.manager.datasource.jdbc.query.QueryCondition;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.row.RowUtil;

import java.util.Properties;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 12:49
 */
public class Test {

    public static void main(String[] args) {
        KToolsContext instance = KToolsContext.getInstance();

        Properties properties = new Properties();
        properties.put("jdbcUrl", "jdbc:impala://172.16.10.173:21050/default");
        properties.put("username", "test");
        properties.put("password", "tes");

        try {
            KDataSourceFactory factory = instance.getDataSourceManager().getFactory("IMPALA");
            KDataSourceHandler dataSourceHandler = factory.createDataSourceHandler(properties);
            dataSourceHandler.conn();
//            System.out.println(dataSourceHandler.selectAllSchema());
//            System.out.println(dataSourceHandler.selectAllTable("default"));
//            System.out.println(dataSourceHandler.selectTableMetadata("default", "test"));
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setPageNum(1L);
            queryCondition.setPageSize(10L);
//            queryCondition.setWhereCondition(" bond_id = '111808224 '");
//            Page<Row> rowPage = dataSourceHandler.selectData("default", "ads_bond_info", queryCondition);
            Page<Row> rowPage = dataSourceHandler.selectData("fidp_fda", "available_quality_bond", queryCondition);
            RowUtil.printPretty(rowPage.getRecords());
            dataSourceHandler.disConn();
        } catch (KToolException e) {
            throw new RuntimeException(e);
        } finally {
            instance.showdown();
        }
    }

}
