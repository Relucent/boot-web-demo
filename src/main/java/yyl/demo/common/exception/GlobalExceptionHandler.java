package yyl.demo.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.relucent.base.plug.model.Result;
import com.github.relucent.base.util.exception.ErrorType;
import com.github.relucent.base.util.exception.GeneralException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected Result<?> handleGlobalException(Exception exception) {
        log(exception);
        String message = exception.getMessage();
        if (message == null) {
            message = "Service Error !";
        }
        return Result.error(message);
    }

    protected void log(Exception e) {
        if (e instanceof GeneralException && ErrorType.PROMPT.equals(((GeneralException) e).getType())) {
            log.warn(e.getMessage());
        } else if (e instanceof org.apache.catalina.connector.ClientAbortException) {
            log.warn(e.toString());
        } else {
            log.error("!", e);
        }
    }
}
