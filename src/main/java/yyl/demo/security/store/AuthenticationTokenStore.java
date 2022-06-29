package yyl.demo.security.store;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.github.relucent.base.common.identifier.IdUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import yyl.demo.properties.WebSecurityProperties;
import yyl.demo.security.model.AccessToken;
import yyl.demo.security.model.UserPrincipal;

/**
 * 身份验证令牌存储器
 */
public class AuthenticationTokenStore {

	// ==============================Fields===========================================
	private final Cache<String, UserPrincipal> userPrincipalCache;

	// ==============================Constructors=====================================
	/**
	 * 构造函数
	 * @param properties 配置参数
	 */
	public AuthenticationTokenStore(WebSecurityProperties properties) {
		Duration ttl = properties.getAccessTokenTtl();
		Duration maxIdleTime = properties.getAccessTokenMaxIdleTime();
		userPrincipalCache = CacheBuilder.newBuilder()//
				.expireAfterWrite(ttl.toMillis(), TimeUnit.MILLISECONDS)//
				.expireAfterAccess(maxIdleTime.toMillis(), TimeUnit.MILLISECONDS)//
				.build();
	}

	// ==============================Methods==========================================
	/**
	 * 存储用户信息，并获得身份令牌
	 * @param principal 用户信息
	 * @return 访问令牌信息
	 */
	public AccessToken putPrincipal(UserPrincipal principal) {

		// 匿名用户
		if (UserPrincipal.isAnonymousUser(principal)) {
			return null;
		}
		String tokenValue = IdUtil.uuid32() + IdUtil.timeId();
		AccessToken token = new AccessToken();
		token.setValue(tokenValue);
		userPrincipalCache.put(tokenValue, principal);
		return token;
	}

	/**
	 * 根据访问令牌获取用户信息
	 * @param tokenValue 访问令牌值
	 * @return 用户信息
	 */
	public UserPrincipal getPrincipal(String tokenValue) {
		return userPrincipalCache.getIfPresent(tokenValue);
	}

	/**
	 * 移除访问令牌
	 * @param tokenValue 访问令牌值
	 */
	public void removeToken(String tokenValue) {
		if (StringUtils.isNotEmpty(tokenValue)) {
			userPrincipalCache.invalidate(tokenValue);
		}
	}
}
