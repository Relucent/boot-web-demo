package yyl.demo.common.encryptor;

/** 配置解密异常 */
@SuppressWarnings("serial")
public final class ConfigDecryptionException extends RuntimeException {

	public enum Reason {
		MISSING_PASSWORD, INVALID_CONFIGURATION, INVALID_ENVELOPE,
	}

	private final Reason reason;

	public ConfigDecryptionException(Reason reason) {
		this(reason, null);
	}

	private ConfigDecryptionException(Reason reason, String propertyName) {
		super("ENC configuration error [" + reason + "]"
				+ (propertyName == null ? "" : " at property " + safeName(propertyName)));
		this.reason = reason;
	}

	public Reason reason() {
		return reason;
	}

	protected ConfigDecryptionException atProperty(String propertyName) {
		return new ConfigDecryptionException(reason, propertyName);
	}

	private static String safeName(String name) {
		return name.length() <= 200 && name.matches("[A-Za-z0-9_.\\[\\]-]+") ? name : "<redacted-key>";
	}
}
