package yyl.demo.common.advice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.relucent.base.common.exception.GeneralException;
import com.github.relucent.base.common.exception.PromptException;
import com.github.relucent.base.plugin.model.Result;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理器<br>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    private final ExceptionAdviceResolver[] resolvers;

    public GlobalExceptionAdvice() {
        List<ExceptionAdviceResolver> resolvers = new ArrayList<>();
        configure(resolvers);
        this.resolvers = resolvers.toArray(new ExceptionAdviceResolver[0]);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected Result<?> handleGlobalException(Exception exception) {
        for (ExceptionAdviceResolver resolver : resolvers) {
            if (resolver.test(exception)) {
                Result<?> vo = resolver.handle(exception);
                if (vo != null) {
                    return vo;
                }
            }
        }
        if (exception instanceof RuntimeException) {
            log.error("!", exception);
        }
        return Result.errorMessage("Service Error!");
    }

    /**
     * 初始化异常处理列表
     * @return 异常处理列表
     */
    protected void configure(List<ExceptionAdviceResolver> resolvers) {

        resolvers.add(new ExceptionAdviceResolver(PromptException.class::isInstance, exception -> {
            final String message = exception.getMessage();
            log.warn(message);
            return Result.errorMessage(message);
        }));
        resolvers.add(new ExceptionAdviceResolver(GeneralException.class::isInstance, exception -> {
            GeneralException gException = (GeneralException) exception;
            Integer code = gException.getCode();
            String message = gException.getMessage();
            log.warn(message);
            if (code == null || code.intValue() == 0) {
                code = 500;
            }
            return Result.of(code, message, StringUtils.EMPTY);
        }));

        resolvers.add(new ExceptionAdviceResolver(HttpRequestMethodNotSupportedException.class::isInstance, exception -> {
            String message = "Request Method Error!";
            log.error("!", exception);
            return Result.errorMessage(message);
        }));

        resolvers.add(new ExceptionAdviceResolver(HttpMessageNotReadableException.class::isInstance, exception -> {
            Throwable cause = exception.getCause();
            String message = "Request Format Error!";
            if (cause instanceof JsonParseException) {
                message = "Request Json Format Error!";
            }
            log.error("!", exception);
            return Result.errorMessage(message);
        }));

        resolvers.add(new ExceptionAdviceResolver(MethodArgumentNotValidException.class::isInstance, exception -> {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = validException.getBindingResult();
            String message = getErrorMessage(bindingResult);
            if (StringUtils.isEmpty(message)) {
                message = "Valid Error!";
            }
            log.warn(message);
            return Result.errorMessage(message);
        }));

        resolvers.add(new ExceptionAdviceResolver(Errors.class::isInstance, exception -> {
            Errors errors = (Errors) exception;
            String message = getErrorMessage(errors);
            if (StringUtils.isEmpty(message)) {
                message = "Valid Errors!";
            }
            log.warn(message);
            return Result.errorMessage(message);
        }));

        resolvers.add(new ExceptionAdviceResolver(exception -> {
            Throwable error = (Throwable) exception;
            for (int i = 0; i < 0xFF && error != null; i++) {
                if (error instanceof SQLException) {
                    return true;
                }
                error = error.getCause();
            }
            return false;
        }, exception -> {
            log.error("!", exception);
            return Result.errorMessage("Data Sql Exception!");
        }));
    }

    /**
     * 获得异常消息
     * @param errors 验证错误信息
     * @return 异常消息
     */
    protected static String getErrorMessage(Errors errors) {
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
     * 异常处理
     */
    @AllArgsConstructor
    private static class ExceptionAdviceResolver {
        private final Predicate<Exception> test;
        private final Function<Exception, Result<?>> handler;

        public boolean test(Exception exception) {
            return test.test(exception);
        }

        public Result<?> handle(Exception exception) {
            return handler.apply(exception);
        }
    }
}
