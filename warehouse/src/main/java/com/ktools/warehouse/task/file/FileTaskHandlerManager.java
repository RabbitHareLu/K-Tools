package com.ktools.warehouse.task.file;

import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.task.file.adapter.CsvFileHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author WCG
 */
public class FileTaskHandlerManager {

    private final static Map<String, Class<? extends FileTaskHandler>> FILE_HANDLER_CACHE = new HashMap<>();

    static {
        FILE_HANDLER_CACHE.put(CsvFileHandler.FILE_TYPE.toUpperCase(), CsvFileHandler.class);
    }

    public static Set<String> getAllFileType() {
        return FILE_HANDLER_CACHE.keySet();
    }

    public static FileTaskHandler getFileTaskHandler(String fileType) throws KToolException {
        if (FILE_HANDLER_CACHE.containsKey(fileType)) {
            try {
                return FILE_HANDLER_CACHE.get(fileType).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new KToolException("暂不支持该文件类型！");
            }
        }
        throw new KToolException("暂不支持该文件类型！");
    }

    public static String parseFileType(String filePath) throws KToolException {
        for (String fileType : FILE_HANDLER_CACHE.keySet()) {
            if (filePath.toUpperCase().endsWith(".".concat(fileType))) {
                return fileType;
            }
        }
        throw new KToolException("暂不支持该文件类型！");
    }
}
