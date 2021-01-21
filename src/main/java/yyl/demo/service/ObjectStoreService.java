package yyl.demo.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.common.web.ContentTypeEnum;
import com.github.relucent.base.common.web.WebUtil.DownloadFile;
import com.github.relucent.base.common.web.WebUtil.DownloadSimpleFile;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import yyl.demo.common.configuration.properties.StoreProperties;

/**
 * 对象存储服务
 */
@Component
@Slf4j
public class ObjectStoreService {

    // ==============================Fields===========================================
    @Autowired
    private StoreProperties storeProperties;

    private String storeDirectory;

    // ==============================Methods==========================================
    /**
     * 存储对象
     * @param bucket 对象桶
     * @param name 对象名称
     * @param input 输入流
     * @param contentType 内容类型
     */
    public void putObject(String bucket, String name, InputStream input, String contentType) {
        try {
            File file = getAbsoluteFile(bucket, name);
            File meta = getAbsoluteMeta(bucket, name);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setWritable(true, true);
            file.setReadable(true, false);
            long length = 0L;
            try (OutputStream output = FileUtils.openOutputStream(file)) {
                length = IOUtils.copyLarge(input, output);
            }
            String metaJson = JsonUtil.encode(new ObjectMeta().setBucket(bucket).setName(name).setContentType(contentType).setLength(length));
            FileUtils.write(meta, metaJson, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("!", e);
        }
    }

    /**
     * 删除对象
     * @param bucket 对象桶
     * @param name 对象名称
     */
    public void removeObject(String bucket, String name) {
        File file = getAbsoluteFile(bucket, name);
        File meta = getAbsoluteMeta(bucket, name);
        FileUtils.deleteQuietly(file);
        FileUtils.deleteQuietly(meta);
    }

    /**
     * 获取对象
     * @param bucket 对象桶
     * @param name 对象名称
     * @return 对象流
     */
    public InputStream getObject(String bucket, String name) {
        File file = getAbsoluteFile(bucket, name);
        try {
            if (file.canRead()) {
                return FileUtils.openInputStream(file);
            }
        } catch (IOException e) {
            log.error("!", e);
        }
        return new ByteArrayInputStream(new byte[0]);
    }

    /**
     * 获取对象元数据
     * @param bucket 对象桶
     * @param name 对象名称
     * @return 对象元数据
     */
    public ObjectMeta getMeta(String bucket, String name) {
        try {
            File meta = getAbsoluteMeta(bucket, name);
            String json = FileUtils.readFileToString(meta, StandardCharsets.UTF_8);
            return JsonUtil.decode(json, ObjectMeta.class);
        } catch (Exception e) {
            log.error("!", e);
        }
        return new ObjectMeta()//
                .setBucket(bucket)//
                .setName(name)//
                .setLength(0)//
                .setContentType(ContentTypeEnum.WILD_CARD.mimeType());
    }

    /**
     * 获得下载实体
     * @param meta 文件信息
     * @return 下载实体
     */
    public DownloadFile getDownloadFile(String bucket, String name) {
        File file = getAbsoluteFile(bucket, name);
        if (file == null) {
            return getNullDownloadFile();
        }
        ObjectMeta meta = getMeta(bucket, name);
        return new DownloadLocalFile(file, meta.getContentType());
    }

    /**
     * 获得一个空文件
     * @return 空文件
     */
    public DownloadFile getNullDownloadFile() {
        return new DownloadSimpleFile("null", "text/plain", new byte[0]);
    }

    private File getAbsoluteFile(String bucket, String name) {
        return new File(storeDirectory, bucket + "/" + name);
    }

    private File getAbsoluteMeta(String bucket, String name) {
        return getAbsoluteFile(bucket, name + ".m");
    }

    @PostConstruct
    protected void initialize() {
        storeDirectory = storeProperties.getDirectory();
    }

    // ==============================InnerClass=======================================
    @SuppressWarnings("serial")
    @Data
    @Accessors(chain = true)
    public static class ObjectMeta implements Serializable {
        /** 对象桶 */
        private String bucket;
        /** 对象名 */
        private String name;
        /** 对象尺寸 */
        private long length;
        /** 对象内容类型 */
        private String contentType;
    }

    private static final class DownloadLocalFile implements DownloadFile {
        private final String name;
        private final String contentType;
        private final Long length;
        private final File file;

        public DownloadLocalFile(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
            this.length = file.length();
            this.name = FilenameUtils.getName(file.getName());
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public long getLength() {
            return length;
        }

        @Override
        public void writeTo(OutputStream output) {
            try (FileInputStream input = FileUtils.openInputStream(file)) {
                IOUtils.copy(input, output);
            } catch (IOException e) {
                log.error("!", e);
            }
        }
    }
}