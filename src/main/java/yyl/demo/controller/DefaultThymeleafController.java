package yyl.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.relucent.base.util.web.WebUtil;

/**
 * 默认_Thymeleaf 视图层。 (使用 _Thymeleaf渲染后缀为 .html 的页面)
 * 
 * @author YYL
 */
@Controller
public class DefaultThymeleafController {

    @RequestMapping(value = "/**/**.html")
    public String route(HttpServletRequest request, HttpServletResponse response) {
        String path = WebUtil.getPathWithinApplication(request);
        return path.substring(1, path.length() - 5);
    }
}
