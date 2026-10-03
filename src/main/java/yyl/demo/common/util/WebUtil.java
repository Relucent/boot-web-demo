package yyl.demo.common.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebUtil {
	/**
	 * 向页面返回 JSON 格式数据
	 * @param json     JSON字符串
	 * @param request  HTTP请求
	 * @param response HTTP响应
	 * @throws IOException IO异常
	 */
	public static void writeJson(String json, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(json);
	}
}
