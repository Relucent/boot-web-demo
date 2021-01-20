package yyl.demo.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;

@RestController
public class GlobalErrorController implements ErrorController {

    private static final String ERROR_ATTRIBUTE_NAME = "javax.servlet.error.status_code";

    @RequestMapping("/error")
    public Result<?> handleError(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute(ERROR_ATTRIBUTE_NAME);
        if (status.intValue() == 404) {
            return Result.of(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
        }
        return Result.error();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}