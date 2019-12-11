package yyl.demo.common.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.github.relucent.base.plugin.model.Result;

public class GlobalErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";
    private static final String ERROR_ATTRIBUTE_NAME = "javax.servlet.error.status_code";

    @DeleteMapping(ERROR_PATH)
    @GetMapping(ERROR_PATH)
    @PostMapping(ERROR_PATH)
    @PutMapping(ERROR_PATH)
    public Result<Object> handleError(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute(ERROR_ATTRIBUTE_NAME);
        if (status.intValue() == 404) {
            return Result.of(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
        }
        return Result.error();
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}