package yyl.demo.common.expection;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.github.relucent.base.util.expection.ErrorType;
import com.github.relucent.base.util.expection.GeneralException;
import com.github.relucent.base.util.json.JsonUtil;
import com.github.relucent.base.util.model.Result;
import com.github.relucent.base.util.web.WebUtil;


/**
 * spring_mvc 异常处理
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    // ==============================Fields===========================================
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected String errorPage = "/_common/500";

    // ==============================Methods==========================================
    /**
     * 处理异常
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logException(ex);
        if (isRestful(request)) {
            try {
                String message = ex.getMessage();
                if (message == null) {
                    message = "Service Error !";
                }
                Result<?> result = Result.error(message);
                String json = JsonUtil.encode(result);
                WebUtil.writeJson(json, request, response);
            } catch (Exception e) {
                logger.error("!", e);
            }
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("errorMsg", ex.getMessage());
            return new ModelAndView(errorPage, map);
        }
        return new ModelAndView();
    }

    /**
     * 异常日志
     * @param ex 异常
     */
    protected void logException(Exception ex) {
        if (ex instanceof ClientAbortException) {
            logger.warn(ex.getMessage());
        } else if (ex instanceof GeneralException && ErrorType.PROMPT.equals(((GeneralException) ex).getType())) {
            logger.warn(ex.getMessage());
        } else {
            logger.error("!", ex);
        }
    }

    protected boolean isRestful(HttpServletRequest request) {
        return WebUtil.isAjax(request);
    }

    // ==============================IocMethods=======================================
    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
