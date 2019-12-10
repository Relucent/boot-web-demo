package yyl.demo.common.util;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * 注解工具类
 */
public class AnnotationUtil {

    // ==============================Constructors=====================================
    protected AnnotationUtil() {
    }

    // ==============================Methods==========================================
    /**
     * 获得处理方法指定注解（包括所属类的注解）
     * @param 处理程序方法 {@link HandlerMethod}
     * @param annotationType 注解类型
     * @return 指定的注解
     */
    public static <A extends Annotation> A getAnnotation(HandlerMethod handler, Class<A> annotationType) {
        A annotation = handler.getMethodAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        Class<?> beanType = handler.getBeanType();
        annotation = AnnotationUtils.findAnnotation(beanType, annotationType);
        return annotation;
    }
}
