package yyl.demo.common.encryptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import yyl.demo.common.encryptor.ConfigDecryptionException.Reason;

/**
 * 配置解密处理器， 在 Spring 环境对象 Environment 初始化完成之后、Bean 定义加载之前执行，对配置进行解密
 * @see ConfigData
 * @see StandardPBEStringEncryptor
 */
public final class ConfigEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	// ==========================Methods=============================================
	/**
	 * 获得执行顺序（在 Spring 环境对象 Environment 初始化完成之后、Bean 定义加载之前执行）
	 * @return 执行顺序
	 */
	@Override
	public int getOrder() {
		return ConfigDataEnvironmentPostProcessor.ORDER + 1;
	}

	/**
	 * 对给定的{@code environment}进行后处理
	 * @param environment 后处理的环境
	 * @param application 环境所属的应用程序
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		// 输出日志，用于确认这个 EnvironmentPostProcessor 确实被 Spring Boot 执行了
		boolean debug = Boolean.parseBoolean(System.getProperty("custom.encryptor.debug", "false"));
		// 只有开启 debug 时才输出
		if (debug) {
			System.out.println(">>> ConfigEnvironmentPostProcessor EXECUTED <<<");
		}

		// 收集所有“可能包含加密配置”的 PropertySource 后面统一处理
		List<OriginTrackedMapPropertySource> candidates = new ArrayList<>();
		for (PropertySource<?> source : environment.getPropertySources()) {

			// 已经存在 CompletedPropertySource，说明之前已经执行过一次解密处理，直接 return 避免重复执行解密逻辑
			if (source instanceof CompletedPropertySource) {
				return;
			}
			// 判断这个配置源里面是否真的存在加密值
			if (!(source instanceof OriginTrackedMapPropertySource)) {
				continue;
			}
			OriginTrackedMapPropertySource tracked = (OriginTrackedMapPropertySource) source;
			if (containsEncryptedValue(environment, tracked)) {
				candidates.add(tracked);
			}
		}

		// 没有包含加密配置
		if (candidates.isEmpty()) {
			return; // Plaintext-only applications need no master password or extra property source.
		}

		// 解密器
		StandardPBEStringEncryptor encryptor = encryptor(environment);

		// 准备替换的配置
		List<OriginTrackedMapPropertySource> replacements = new ArrayList<>();
		for (OriginTrackedMapPropertySource source : candidates) {
			Map<String, Object> values = new LinkedHashMap<>(source.getSource());
			for (String name : source.getPropertyNames()) {

				if (isControlKey(name)) {
					continue;
				}

				Object value = source.getProperty(name);
				String resolvedValue = resolveValue(environment, value);

				if (!isCandidate(resolvedValue)) {
					continue;
				}

				String plaintext;
				try {
					// 尝试解密
					plaintext = decrypt((String) resolvedValue, encryptor);
				} catch (ConfigDecryptionException ex) {
					// 解密失败时，把当前配置项名称附加到异常中(更容易定位问题)
					throw ex.atProperty(name);
				}

				// 获得配置值（可能是来源信息OriginTrackedValue）
				Object original = source.getSource().get(name);
				// 把解密后的明文放回新的 values Map
				values.put(name,
						original instanceof OriginTrackedValue
								? OriginTrackedValue.of(plaintext, source.getOrigin(name))
								: plaintext);
			}
			replacements.add(new OriginTrackedMapPropertySource(source.getName(), values, source.isImmutable()));
		}

		// 将 Environment 中原来的 PropertySource 替换成解密后的版本
		for (OriginTrackedMapPropertySource replacement : replacements) {
			environment.getPropertySources().replace(replacement.getName(), replacement);
		}

		// 最后添加一个“完成标记”
		environment.getPropertySources().addLast(new CompletedPropertySource());
	}

	// ==========================PrivateMethods======================================
	private static boolean containsEncryptedValue(ConfigurableEnvironment environment,
			OriginTrackedMapPropertySource source) {
		for (String name : source.getPropertyNames()) {
			if (isControlKey(name)) {
				continue;
			}
			String resolvedValue = resolveValue(environment, source.getProperty(name));
			if (isCandidate(resolvedValue)) {
				return true;
			}
		}

		return false;
	}

	private static StandardPBEStringEncryptor encryptor(ConfigurableEnvironment environment) {

		String password = property(environment, "custom.encryptor.password", null);

		if (password == null) {
			password = environment.getProperty("CUSTOM_ENCRYPTOR_PASSWORD");
		}

		if (password == null || password.isEmpty()) {
			throw new ConfigDecryptionException(Reason.MISSING_PASSWORD);
		}
		if (isCandidate(password) || password.contains("${")) {
			throw new ConfigDecryptionException(Reason.INVALID_CONFIGURATION);
		}

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		encryptor.setIvGenerator(new RandomIvGenerator());
		encryptor.setPassword(password);
		return encryptor;
	}

	private static String property(ConfigurableEnvironment environment, String name, String fallback) {
		// Spring relaxed binding also handles existing camelCase and environment-variable spellings.
		return Binder.get(environment).bind(name, String.class).orElse(fallback);
	}

	private static String resolveValue(ConfigurableEnvironment environment, Object value) {
		if (!(value instanceof String)) {
			return null;
		}
		return environment.resolvePlaceholders((String) value);
	}

	private static boolean isCandidate(Object value) {
		return value instanceof String && ((String) value).contains("ENC(");
	}

	private static boolean isControlKey(String name) {
		return name.toLowerCase(java.util.Locale.ROOT).replaceAll("[._-]", "").startsWith("customencryptor");
	}

	private static String decrypt(String value, StandardPBEStringEncryptor encryptor) {
		if (value == null || value.length() > 65_536) {
			throw new ConfigDecryptionException(Reason.INVALID_ENVELOPE);
		}
		String trimmed = value.trim();
		if (!trimmed.startsWith("ENC(") || !trimmed.endsWith(")")) {
			throw new ConfigDecryptionException(Reason.INVALID_ENVELOPE);
		}
		String encrypted = trimmed.substring("ENC(".length(), trimmed.length() - 1);
		return encryptor.decrypt(encrypted);
	}

	// ==========================InnerClassMethods===================================
	/**
	 * 完成标记，用来防止在第二次处理时将解密的明文ENC文本误认为是密文
	 */
	private static final class CompletedPropertySource extends PropertySource<Object> {
		private CompletedPropertySource() {
			super(CompletedPropertySource.class.getName(), new Object());
		}

		@Override
		public Object getProperty(String name) {
			return null;
		}
	}

}
