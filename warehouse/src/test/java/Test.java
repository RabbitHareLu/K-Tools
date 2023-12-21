import com.ktools.KToolsContext;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.KDataSourceFactory;
import com.ktools.manager.datasource.KDataSourceHandler;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;

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
            TableMetadata tableMetadata = dataSourceHandler.selectTableMetadata("default", "test");
            System.out.println(tableMetadata);
            dataSourceHandler.disConn();
        } catch (KToolException e) {
            throw new RuntimeException(e);
        }
    }

}
