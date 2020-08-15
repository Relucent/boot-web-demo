package yyl.demo.common.exception;

import java.sql.SQLException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.relucent.base.common.exception.PromptException;
import com.github.relucent.base.plugin.model.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected Result<?> handleGlobalException(Exception exception) {
        log(exception);
        if (exception instanceof PromptException) {
            return Result.error(exception.getMessage());
        }
        if (exception instanceof HttpMessageNotReadableException) {
            Throwable cause = exception.getCause();
            if (cause instanceof JsonParseException) {
                return Result.error("Request Json Format Error!");
            } else {
                return Result.error("Request Format Error!");
            }
        }
        if (isDbException(exception)) {
            return Result.error("Data Exception!");
        }
        return Result.error("Service Error!");
    }

    protected void log(Exception e) {
        if (e instanceof PromptException) {
            log.warn(e.getMessage());
        } else {
            log.error("!", e);
        }
    }

    private static boolean isDbException(Exception e) {
        Throwable error = (Throwable) e;
        for (int i = 0; i < 0xFF && error != null; i++) {
            if (error instanceof SQLException) {
                return true;
            }
            error = error.getCause();
        }
        return false;
    }
}
