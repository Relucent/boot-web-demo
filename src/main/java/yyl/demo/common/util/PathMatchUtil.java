package yyl.demo.common.util;

import org.springframework.util.AntPathMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * 增加路径匹配工具类
 */
@Slf4j
public class PathMatchUtil {

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 判断路径是否匹配模式
     * @param pattern 模式数组
     * @param path 路径
     * @return 任意一个模式与路径匹配都返回true，如果模式否不匹配返回false
     */
    public static boolean matchAny(String[] patterns, String path) {
        for (String pattern : patterns) {
            try {
                if (ANT_PATH_MATCHER.match(pattern, path)) {
                    return true;
                }
            } catch (Exception e) {
                log.warn("!", e);
            }
        }
        return false;
    }

    /**
     * 判断路径是否匹配模式
     * @param pattern 模式
     * @param path 路径
     * @return 路径是否匹配
     */
    public static boolean match(String pattern, String path) {
        return ANT_PATH_MATCHER.match(pattern, path);
    }
}
