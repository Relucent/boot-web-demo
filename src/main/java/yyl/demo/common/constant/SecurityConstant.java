package yyl.demo.common.constant;

/**
 * 权限常量
 */
public class SecurityConstant {

    /** 角色前缀 */
    public static final String ROLE_PREFIX = "ROLE_";

    /** HTTP Header 中访问令牌的名称 */
    public static final String ACCESS_TOKEN_HEADER = "Access-Token";

    /** HTTP Header 认证头名称 */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /** HTTP 认证方案 */
    public enum HttpAuthorizationScheme {
        Basic, Bearer;
    }
}
