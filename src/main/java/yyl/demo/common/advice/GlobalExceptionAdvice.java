package yyl.demo.common.advice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.relucent.base.common.exception.GeneralException;
import com.github.relucent.base.common.exception.PromptException;
import com.github.relucent.base.common.lang.StringUtil;
import com.github.relucent.base.plugin.model.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理器<br>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(GeneralException.class)
    protected Result<?> handle(GeneralException exception) {
        log.error("!", exception);
        Integer code = exception.getCode();
        String message = exception.getMessage();
        code = toErrorCode(code);
        return Result.ofMessage(code, message);
    }

    @ExceptionHandler(PromptException.class)
    protected Result<?> handle(PromptException exception) {
        Integer code = exception.getCode();
        String message = exception.getMessage();
        code = toErrorCode(code);
        log.warn(message);
        return Result.ofMessage(code, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected Result<?> handle(HttpMessageNotReadableException exception) {
        Throwable cause = exception.getCause();
        String message = "Request Format Error!";
        if (cause instanceof JsonParseException) {
            message = "Request Json Format Error!";
        }
        log.error("!", exception);
        return Result.errorMessage(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Result<?> handle(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        String message = getErrorMessage(bindingResult);
        if (StringUtil.isEmpty(message)) {
            message = "Valid Error!";
        }
        log.warn(message);
        return Result.errorMessage(message);
    }

    @ExceptionHandler(Exception.class)
    protected Result<?> handle(Exception exception) {
        if (exception instanceof Errors) {
            Errors errors = (Errors) exception;
            String message = getErrorMessage(errors);
            if (StringUtil.isEmpty(message)) {
                message = "Valid Errors!";
            }
            log.warn(message);
            return Result.errorMessage(message);
        }

        log.error("!", exception);
        if (isSqlException(exception)) {
            return Result.errorMessage("Data Sql Exception!");
        }
        return Result.errorMessage("Service Error!");
    }

    /**
     * 判断是否SQL异常
     * @param throwable 异常
     * @return 是否SQL异常
     */
    private static boolean isSqlException(Throwable throwable) {
        for (int i = 0; i < 0xFF && throwable != null; i++) {
            if (throwable instanceof SQLException) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }

    /**
     * 获得异常消息
     * @param errors 验证错误信息
     * @return 异常消息
     */
    private static String getErrorMessage(Errors errors) {
        if (errors == null) {
            return null;
        }
        List<String> errorMessageList = new ArrayList<>();
        for (ObjectError error : errors.getAllErrors()) {
            errorMessageList.add(error.getDefaultMessage());
        }
        return errorMessageList.stream().collect(Collectors.joining("; "));
    }

    /**
     * 确保返回的是异常的编码
     * @param code 原始编码
     * @return 异常的编码
     */
    private static Integer toErrorCode(Integer code) {
        if (null == code || 0 == code.intValue() || 200 == code.intValue()) {
            return 500;
        }
        return code;
    }
}
