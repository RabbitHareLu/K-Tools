import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.KDataSourceFactory;
import com.ktools.warehouse.manager.datasource.KDataSourceHandler;
import com.ktools.warehouse.manager.datasource.KDataSourceManager;
import com.ktools.warehouse.task.TaskContext;
import com.ktools.warehouse.task.job.TaskResult;
import com.ktools.warehouse.task.model.Job;
import com.ktools.warehouse.task.model.JobSinkConfig;
import com.ktools.warehouse.task.model.JobSourceConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Slf4j
public class Test {

    public static void main(String[] args) {
        KToolsContext instance = KToolsContext.getInstance();

        Properties properties = new Properties();
        properties.put("jdbcUrl", "jdbc:impala://172.16.10.84:21050/default;UseSasl=0;AuthMech=0;UID=impala;PWD=impala");
        properties.put("impala.kuduMaster", "172.16.10.84:7051,172.16.10.85:7051,172.16.10.86:7051");
        properties.put("username", "test");
        properties.put("password", "tes");

        try {
            KDataSourceFactory factory = instance.getDataSourceManager().getFactory("IMPALA");
            KDataSourceHandler dataSourceHandler = factory.createDataSourceHandler(properties);
            dataSourceHandler.conn();

            KDataSourceManager dataSourceManager = instance.getDataSourceManager();
            dataSourceManager.addHandler("impala-test", dataSourceHandler);

            Job job = getJob();

            Future<TaskResult> future = TaskContext.submit(job);

            TaskResult taskResult = future.get();
            log.info("数据量：" + taskResult.getCount());
            log.info("执行时间：" + taskResult.getTaskExecTime() + "ms");
        } catch (KToolException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            instance.showdown();
        }
    }

    private static Job getJob() {
        Job job = new Job();
        job.setJobId("test");
        job.setJobName("测试");

//        JobSourceConfig jobSourceConfig = new JobSourceConfig();
//        jobSourceConfig.setSourceType("IMPALA");
//        jobSourceConfig.setSourceId("impala-test");
//        jobSourceConfig.setSourceSchema("test");
//        jobSourceConfig.setSourceTableName("test_from");
//        jobSourceConfig.setViewSql("select * from default.test_from");

        JobSourceConfig jobSourceConfig = new JobSourceConfig();
        jobSourceConfig.setSourceType("file");
        jobSourceConfig.setFilePath("E:\\Java\\default_test_from_1704360637164.csv");
        jobSourceConfig.setSeparator(',');
        job.setSourceConfig(jobSourceConfig);

//        JobSinkConfig jobSinkConfig = new JobSinkConfig();
//        jobSinkConfig.setSinkType("file");
//        jobSinkConfig.setSinkFileType("CSV");
//        jobSinkConfig.setSeparator(',');
//        jobSinkConfig.setFileSinkPath("E:\\Java");
//        jobSinkConfig.setSinkSchema("default");
//        jobSinkConfig.setSinkTableName("test_from");

        JobSinkConfig jobSinkConfig = new JobSinkConfig();
        jobSinkConfig.setSinkId("impala-test");
        jobSinkConfig.setSinkType("IMPALA");
        jobSinkConfig.setSinkSchema("test");
        jobSinkConfig.setSinkTableName("test_to");
        job.setSinkConfig(jobSinkConfig);
        return job;
    }

}
