package yyl.demo.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.relucent.base.common.exception.ExceptionHelper;

import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.common.util.IdUtil;
import yyl.demo.component.FileStoreComponent;
import yyl.demo.component.FileStoreComponent.FileMeta;
import yyl.demo.entity.FileEntity;
import yyl.demo.kit.FileKit;
import yyl.demo.mapper.FileMapper;
import yyl.demo.model.vo.FileVO;
import yyl.demo.security.Securitys;

/**
 * 文件存储服务
 */
@Service
public class FileService {

    // ==============================Fields===========================================

    private static final String DATE_FOLDER_PATTERN = "yyyy'/'MM'/'dd";

    @Autowired
    private FileStoreComponent fileStoreComponent;

    @Autowired
    private FileMapper fileMapper;

    // ==============================Methods==========================================

    /**
     * 存储文件
     * @param file 文件对象
     * @return 文件信息
     */
    public FileVO put(MultipartFile file) {

        // 文件ID
        String id = IdUtil.timeId();

        // 生成文件存储路径
        String path = createPath(id);
        String filename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename);
        String contentType = file.getContentType();
        Long length = file.getSize();

        try (InputStream input = file.getInputStream()) {
            fileStoreComponent.put(path, input);
        } catch (IOException e) {
            throw ExceptionHelper.prompt("文件上传失败");
        }

        FileEntity entity = new FileEntity();
        entity.setId(id);
        entity.setName(filename);
        entity.setExtension(extension);
        entity.setLength(length);
        entity.setContentType(contentType);
        entity.setPath(path);

        String identity = Securitys.getUserId();
        AuditableUtil.setCreated(entity, identity);
        fileMapper.insert(entity);

        return FileKit.toVO(entity);
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    public void deleteById(String id) {
        FileEntity entity = fileMapper.getById(id);
        if (entity == null) {
            return;
        }
        fileMapper.deleteById(id);
        fileStoreComponent.delete(entity.getPath());
    }

    /**
     * @param id 文件ID
     * @return 文件信息
     */
    public FileVO getById(String id) {
        FileEntity entity = fileMapper.getById(id);
        if (entity == null) {
            return null;
        }
        return FileKit.toVO(entity);
    }

    /**
     * 获取文件元信息
     * @param path 文件路径
     * @return 文件元信息
     */
    public FileMeta getFileMeta(String path) {
        return fileStoreComponent.get(path);
    }

    // ==============================ToolMethods======================================
    /**
     * 生成文件路径
     * @return 文件路径
     */
    private String createPath(String id) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(DateFormatUtils.format(System.currentTimeMillis(), DATE_FOLDER_PATTERN)).append("/");
        pathBuilder.append(id);
        return pathBuilder.toString();
    }
}
