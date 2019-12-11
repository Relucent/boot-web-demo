package yyl.demo.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.relucent.base.common.web.ContentTypeEnum;
import com.github.relucent.base.common.web.DownloadFile;
import com.github.relucent.base.common.web.DownloadMode;
import com.github.relucent.base.common.web.WebUtil;
import com.github.relucent.base.plugin.model.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import yyl.demo.component.FileStoreComponent.FileMeta;
import yyl.demo.model.vo.FileVO;
import yyl.demo.service.FileService;

@RequestMapping("/rest/file")
@Api(tags = "文件存储")
@Slf4j
public class FileController {

    // ==============================Fields===========================================
    @Autowired
    private FileService fileService;

    // ==============================Methods==========================================
    @PostMapping(value = "/upload")
    @ApiOperation("上传文件")
    public Result<FileVO> upload(@ApiParam(value = "上传文件", required = true) @RequestPart(value = "file", required = true) MultipartFile file) {
        FileVO vo = fileService.put(file);
        return Result.ok(vo);
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param request HTTP请求
     * @param response HTTP 响应
     */
    @ApiOperation("下载文件")
    @GetMapping(value = "/download")
    public void download(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {
        doDownload(id, request, response, DownloadMode.ATTACHMENT);
    }

    /**
     * 下载文件(页面内嵌)
     * @param id 文件ID
     * @param request HTTP请求
     * @param response HTTP 响应
     */
    @ApiOperation("内嵌文件")
    @GetMapping(value = "/inline")
    public void inline(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {
        doDownload(id, request, response, DownloadMode.INLINE);
    }

    /**
     * 输出文件流
     * @param objectName 对象名（文件存储路径）
     * @param request HTTP请求
     * @param response HTTP 响应
     * @param mode 输出模式
     */
    private void doDownload(String id, HttpServletRequest request, HttpServletResponse response, DownloadMode mode) {
        try {
            FileVO vo = fileService.getById(id);
            if (vo == null) {
                doDownloadNull(request, response, mode);
                return;
            }
            String path = vo.getPath();
            String originalName = vo.getName();
            String contentType = vo.getContentType();

            FileMeta meta = fileService.getFileMeta(path);

            try (InputStream input = meta.openInputStream()) {
                if (input == null) {
                    doDownloadNull(request, response, mode);
                    return;
                }
                DownloadFile file = new DownloadFile(originalName, contentType, input, meta.length());
                WebUtil.download(file, request, response, mode);
            }
        } catch (Exception e) {
            log.error("!", e);
        }
    }

    /**
     * 空白文件下载
     * @param request 请求
     * @param response 响应
     * @param mode 下载模式
     * @throws IOException 出现IO异常
     */
    private static void doDownloadNull(HttpServletRequest request, HttpServletResponse response, DownloadMode mode) throws IOException {
        DownloadFile file = new DownloadFile("null", ContentTypeEnum.TXT.mimeType(), ArrayUtils.EMPTY_BYTE_ARRAY);
        WebUtil.download(file, request, response, mode);
    }
}
