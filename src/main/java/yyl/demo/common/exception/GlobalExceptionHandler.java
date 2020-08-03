package yyl.demo.common.exception;

import java.sql.SQLException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.relucent.base.common.exception.ErrorType;
import com.github.relucent.base.common.exception.GeneralException;
import com.github.relucent.base.plugin.model.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	protected Result<?> handleGlobalException(Exception exception) {
		log(exception);
		final String message;
		if (exception instanceof HttpMessageNotReadableException) {
			Throwable cause = exception.getCause();
			if (cause instanceof JsonParseException) {
				message = "Request Json Format Error!";
			} else {
				message = "Request Format Error!";
			}
		} else if (isDbException(exception)) {
			message = "Data Exception!";
		} else {
			message = "Service Error!";
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
