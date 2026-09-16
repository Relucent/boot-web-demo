package yyl.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.github.relucent.base.common.io.FilenameUtil;
import com.github.relucent.base.common.io.IoUtil;
import com.github.relucent.base.common.web.DownloadMode;
import com.github.relucent.base.plugin.model.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import yyl.demo.component.FileStoreComponent.FileMeta;
import yyl.demo.model.vo.FileVO;
import yyl.demo.service.FileService;

@RequestMapping("/rest/file")
@Tag(name = "文件存储")
@Slf4j
public class FileController {

	// ==============================Fields===========================================
	@Autowired
	private FileService fileService;

	// ==============================Methods==========================================
	@Operation(summary = "上传文件")
	@PostMapping(value = "/upload")
	public Result<FileVO> upload(@RequestPart(value = "file", required = true) MultipartFile file) {
		FileVO vo = fileService.put(file);
		return Result.ok(vo);
	}

	/**
	 * 下载文件
	 * @param id       文件ID
	 * @param request  HTTP请求
	 * @param response HTTP 响应
	 */
	@Operation(summary = "下载文件")
	@GetMapping(value = "/download")
	public ResponseEntity<StreamingResponseBody> download(@RequestParam("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		return doDownload(id, DownloadMode.ATTACHMENT);
	}

	/**
	 * 下载文件(页面内嵌)
	 * @param id       文件ID
	 * @param request  HTTP请求
	 * @param response HTTP 响应
	 */
	@Operation(summary = "内嵌文件")
	@GetMapping(value = "/inline")
	public ResponseEntity<StreamingResponseBody> inline(@RequestParam("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		return doDownload(id, DownloadMode.INLINE);
	}

	// ==============================PrivateMethods===================================
	/**
	 * 输出文件流
	 * @param objectName 对象名（文件存储路径）
	 * @param mode       输出模式
	 * @return 响应实体
	 */
	private ResponseEntity<StreamingResponseBody> doDownload(String id, DownloadMode mode) {
		FileVO vo = fileService.getById(id);
		if (vo == null) {
			return doDownloadNull(mode);
		}
		String path = vo.getPath();
		String originalName = vo.getName();
		String contentType = vo.getContentType();

		FileMeta meta = fileService.getFileMeta(path);
		try (InputStream input = meta.openInputStream()) {
			if (input == null) {
				return doDownloadNull(mode);
			}
			StreamingResponseBody body = output -> {
				IoUtil.copyLarge(input, output);
			};
			ContentDisposition disposition = getContentDisposition(originalName, mode);
			return ResponseEntity.ok()//
					.contentType(MediaType.parseMediaType(contentType))//
					.contentLength(meta.length())//
					.header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())//
					.body(body);
		} catch (IOException e) {
			log.error("!", e);
			return doDownloadNull(mode);
		}
	}

	/**
	 * 空白文件下载
	 * @param mode 下载模式
	 * @return 响应实体
	 */
	private ResponseEntity<StreamingResponseBody> doDownloadNull(DownloadMode mode) {
		ContentDisposition disposition = getContentDisposition("null", mode);
		StreamingResponseBody body = output -> {
		};
		return ResponseEntity.ok()//
				.header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())//
				.contentType(MediaType.TEXT_PLAIN)//
				.body(body);
	}

	/**
	 * 获得内容描述
	 * @param path 文件名(或者文件路径)
	 * @param mode 下载模式
	 * @return 内容描述
	 */
	private static ContentDisposition getContentDisposition(String path, DownloadMode mode) {
		ContentDisposition.Builder builder = DownloadMode.INLINE.equals(mode)//
				? ContentDisposition.inline()//
				: ContentDisposition.attachment();
		String filename = Objects.toString(FilenameUtil.getName(path), "download");
		return builder.filename(filename, StandardCharsets.UTF_8).build();
	}
}
