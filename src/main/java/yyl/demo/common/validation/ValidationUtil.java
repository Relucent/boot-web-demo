package yyl.demo.common.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.github.relucent.base.common.exception.ExceptionUtil;

/**
 * 数据校验工具类
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * 检查错误，如果有错误则抛出异常
     * @param errors 校验结果
     */
    public static void checkErrors(Errors errors) {
        if (errors.hasErrors()) {
            String message = getErrorMessage(errors);
            throw ExceptionUtil.prompt(message);
        }
    }

    /**
     * 获得异常消息
     * @param errors 验证错误信息
     * @return 异常消息
     */
    public static String getErrorMessage(Errors errors) {
        if (errors == null) {
            return null;
        }
        List<String> errorMessageList = new ArrayList<>();
        for (ObjectError error : errors.getAllErrors()) {
            errorMessageList.add(error.getDefaultMessage());
        }
        return StringUtils.join(errorMessageList, "; ");
    }

    /**
     * 非空验证， 如果对象为空，则抛出异常
     * @param obj 需要验证的对象
     * @param message 异常信息
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw ExceptionUtil.prompt(message);
        }
    }

    /**
     * 参数验证 验证参数是否为null和为空
     * @param string 字符串
     * @param message 异常信息
     */
    public static void notEmpty(String string, String message) {
        if (StringUtils.isEmpty(string)) {
            throw ExceptionUtil.prompt(message);
        }
    }

    /**
     * 集合校验， 如果对象为空，则抛出异常
     * @param collection 集合对象
     * @param message 异常信息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw ExceptionUtil.prompt(message);
        }
    }

    /**
     * 抛出异常
     * @param error 是否异常
     * @param message 异常信息
     */
    public static void checkError(boolean error, String message) {
        if (error) {
            throw ExceptionUtil.prompt(message);
        }
    }
}
