package com.ktools.warehouse.task.file;

import com.ktools.warehouse.common.utils.StringUtil;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.task.DataTaskHandler;
import com.ktools.warehouse.task.element.BaseRow;
import com.ktools.warehouse.task.model.JobSinkConfig;
import com.ktools.warehouse.task.model.JobSourceConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author WCG
 */
public abstract class FileTaskHandler implements DataTaskHandler {

    @Override
    public void source(JobSourceConfig sourceConfig, Consumer<Stream<BaseRow>> consumer) throws KToolException {
        String filePath = sourceConfig.getFilePath();
        if (StringUtil.isBlank(filePath)) {
            throw new KToolException("未指定文件！");
        }
        Path path = Path.of(filePath);
        readFile(path, sourceConfig.getSeparator(), consumer);
    }

    protected abstract void readFile(Path path, char separator, Consumer<Stream<BaseRow>> consumer) throws KToolException;

    @Override
    public void sink(JobSinkConfig sinkConfig, Stream<BaseRow> stream) throws KToolException {
        String filePath = sinkConfig.getFileSinkPath();
        if (StringUtil.isBlank(filePath)) {
            throw new KToolException("未指定文件！");
        }
        // 判断文件夹是否存在，不存在则新建
        Path dir = Path.of(filePath);
        try {
            if (Files.exists(dir)) {
                if (!Files.isDirectory(dir)) {
                    throw new KToolException("文件路径非文件夹！");
                }
            } else {
                Files.createDirectories(dir);
            }
            // 构建文件名
            String fileName = sinkConfig.getSinkSchema() + "_" + sinkConfig.getSinkTableName() + "_" + System.currentTimeMillis() + "." + sinkConfig.getSinkFileType().toLowerCase();
            File file = new File(dir.toFile(), fileName);
            if (file.createNewFile()) {
                writeFile(file.toPath(), sinkConfig.getSeparator(), stream);
            } else {
                throw new KToolException("文件夹中创建文件失败");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void writeFile(Path path, char separator, Stream<BaseRow> stream) throws KToolException;

}
