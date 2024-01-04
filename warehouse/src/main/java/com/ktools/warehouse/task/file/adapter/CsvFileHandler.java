package com.ktools.warehouse.task.file.adapter;

import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.task.element.BaseColumn;
import com.ktools.warehouse.task.element.BaseRow;
import com.ktools.warehouse.task.element.DataType;
import com.ktools.warehouse.task.file.FileTaskHandler;
import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.writer.CsvWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * csv文件处理器
 *
 * @author WCG
 */
public class CsvFileHandler extends FileTaskHandler {

    public static final String FILE_TYPE = "CSV";

    /**
     * 读取csv文件
     *
     * @param path      文件路径
     * @param separator 分割符
     * @param consumer  回调函数
     */
    @Override
    protected void readFile(Path path, char separator, Consumer<Stream<BaseRow>> consumer) throws KToolException {
        NamedCsvReader.NamedCsvReaderBuilder namedCsvReaderBuilder = NamedCsvReader.builder().fieldSeparator(separator);
        try (NamedCsvReader csvReader = namedCsvReaderBuilder.build(path)) {
            Set<String> header = csvReader.getHeader();
            Stream<BaseRow> baseRowStream = csvReader.stream().map(namedCsvRow -> {
                BaseRow baseRow = new BaseRow(header.size());
                header.forEach(key -> {
                    BaseColumn baseColumn = BaseColumn.create(key, namedCsvRow.getField(key), DataType.STRING);
                    baseRow.addField(baseColumn);
                });
                return baseRow;
            });
            consumer.accept(baseRowStream);
        } catch (IOException e) {
            throw new KToolException("文件读取异常！", e);
        }
    }

    @Override
    protected void writeFile(Path path, char separator, Stream<BaseRow> stream) throws KToolException {
        try (CsvWriter csvWriter = CsvWriter.builder().fieldSeparator(separator).build(path)) {
            AtomicBoolean header = new AtomicBoolean(true);
            stream.forEach(baseRow -> {
                if (header.compareAndSet(true, false)) {
                    String[] headers = baseRow.getColumns().keySet().toArray(String[]::new);
                    // 写表头
                    csvWriter.writeRow(headers);
                }
                // 写数据
                String[] data = baseRow.getColumns().values().stream()
                        .map(baseColumn -> String.valueOf(baseColumn.getData()))
                        .toArray(String[]::new);
                csvWriter.writeRow(data);
            });
        } catch (IOException e) {
            throw new KToolException("文件写入异常！", e);
        }
    }

}
