package yyl.demo.common.matcher;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.annotation.security.PermitAll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import lombok.extern.slf4j.Slf4j;
import yyl.demo.common.util.AnnotationUtil;
import yyl.demo.common.util.PathMatchUtil;
import yyl.demo.properties.WebSecurityProperties;

/**
 * URL匹配工具类
 */
@Slf4j
public class PermitUrlMatcher {

    // =======================Fields==================================================
    /** 不需要进行身份认证直接可以访问的URL */
    private String[] permitAllUrls = {};

    /** 需要进行身份认证才能访问的URL */
    private String[] authenticatedUrls = { "/**" };

    // =======================Constructors=============================================
    public PermitUrlMatcher(ApplicationContext applicationContext, WebSecurityProperties properties) {
        Set<String> permitAllUrlSet = new TreeSet<>();
        final Pattern pathVariablePattern = Pattern.compile("\\{(.*?)\\}");
        try {
            RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                RequestMappingInfo info = entry.getKey();
                HandlerMethod handler = entry.getValue();
                PermitAll ax = AnnotationUtil.getAnnotation(handler, PermitAll.class);
                if (ax == null) {
                    continue;
                }
                for (String url : info.getPatternsCondition().getPatterns()) {
                    String path = pathVariablePattern.matcher(url).replaceAll("*");
                    permitAllUrlSet.add(path);
                }
            }
        } catch (NoClassDefFoundError e) {
            log.warn("Application not support mvc#RequestMappingHandlerMapping");
        }

        String[] permitAllUrls = properties.getPermitAllUrls();
        String[] endpointUrls = properties.getEndpointUrls();
        if (log.isInfoEnabled()) {
            log.info("[@EndpointUrls=]");
            for (String url : properties.getEndpointUrls()) {
                log.info(url);
            }
            log.info("[@PermitAllUrls=]");
            for (String url : permitAllUrls) {
                log.info(url);
            }
            for (String url : permitAllUrlSet) {
                log.info(url);
            }
        }
        CollectionUtils.addAll(permitAllUrlSet, permitAllUrls);
        CollectionUtils.addAll(permitAllUrlSet, endpointUrls);
        this.permitAllUrls = permitAllUrlSet.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        this.authenticatedUrls = properties.getAuthenticatedUrls();
    }

    // ==========================Methods=============================================
    /**
     * 获得需要认证才能访问的URL地址
     * @return 需要权限拦截的URL地址
     */
    public String[] getAuthenticatedUrls() {
        String[] urls = this.authenticatedUrls;
        return Arrays.copyOf(urls, urls.length);
    }

    /**
     * 获得不需要进行身份认证直接可以访问的URL地址
     * @return 不需要进行身份认证直接可以访问的URL地址
     */
    public String[] getPermitAllUrls() {
        String[] urls = this.permitAllUrls;
        return Arrays.copyOf(urls, urls.length);
    }

    /**
     * 判断路径是否需要身份认证
     * @param path 比较的路径
     * @return 路径是否需要身份认证
     */
    public boolean matchAuthenticatedUrl(String path) {
        return PathMatchUtil.matchAny(authenticatedUrls, path);
    }

    /**
     * 判断路径是否不需要身份认证
     * @param path 比较的路径
     * @return 路径是否不需要身份认证
     */
    public boolean matchPermitUrl(String path) {
        return PathMatchUtil.matchAny(permitAllUrls, path);
    }
}
