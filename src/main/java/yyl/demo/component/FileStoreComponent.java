package yyl.demo.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yyl.demo.properties.FileStoreProperties;

/**
 * 对象存储服务
 */
@Component
@Slf4j
public class FileStoreComponent {

    // ==============================Fields===========================================
    private final String storeDirectory;

    // ==============================Constructors=====================================
    public FileStoreComponent(FileStoreProperties properties) {
        storeDirectory = properties.getDirectory();
    }

    // ==============================Methods==========================================
    /**
     * 存储文件（如果文件路径已存在，则会覆盖旧文件）
     * @param path 文件路径
     * @param input 文件流
     * @return 存储成功返回 true，失败返回 false
     */
    public boolean put(String path, InputStream input) {
        try {
            File file = getAbsoluteFile(path);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setWritable(true, true);
            file.setReadable(true, false);
            try (OutputStream output = FileUtils.openOutputStream(file)) {
                IOUtils.copyLarge(input, output);
            }
            return true;
        } catch (Exception e) {
            log.error("!", e);
            return false;
        }
    }

    /**
     * 删除文件
     * @param path 文件路径
     */
    public void delete(String path) {
        File file = getAbsoluteFile(path);
        FileUtils.deleteQuietly(file);
    }

    /**
     * 获取文件信息
     * @param path 文件路径
     * @return 文件元信息
     */
    public FileMeta get(String path) {
        File file = getAbsoluteFile(path);
        return new FileMeta(file);
    }

    // ==============================ToolMethods======================================
    /**
     * 获得文件对象(绝对路径)
     * @param path 文件路径
     * @return 文件对象
     */
    private File getAbsoluteFile(String path) {
        return new File(storeDirectory, path);
    }

    // ==============================InnerClass=======================================
    @AllArgsConstructor
    public static class FileMeta {

        private final File file;

        public long length() {
            return file.length();
        }

        public InputStream openInputStream() {
            try {
                return FileUtils.openInputStream(file);
            } catch (IOException e) {
                log.error("!", e);
            }
            return null;
        }
    }
}