package yyl.demo.security.store;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import yyl.demo.common.util.IdUtil;
import yyl.demo.common.util.RsaUtil;

/**
 * 公钥私钥存储
 */
@Slf4j
public class RsaKeyPairStore {

    // ==============================Fields===========================================
    private final Cache<String, KeyPairElement> cache;

    // ==============================Constructors=====================================
    /**
     * 构造函数
     */
    public RsaKeyPairStore() {
        Duration ttl = Duration.parse("PT30S");
        cache = CacheBuilder.newBuilder()//
                .expireAfterWrite(ttl.toMillis(), TimeUnit.MILLISECONDS)//
                .expireAfterAccess(ttl.toMillis(), TimeUnit.MILLISECONDS)//
                .build();
    }

    // ==============================Methods==========================================
    /**
     * 存储密钥对
     * @param keyPair 密钥对
     * @return 存储ID
     */
    public String put(KeyPair keyPair) {
        String id = IdUtil.uuid32();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        KeyPairElement element = new KeyPairElement();
        element.setPublicKey(RsaUtil.encodeBase64String(publicKey));
        element.setPrivateKey(RsaUtil.encodeBase64String(privateKey));
        cache.put(id, element);
        return id;
    }

    /**
     * 根据ID获取密钥对
     * @param id 密钥对ID
     * @return 密钥对
     */
    public KeyPair get(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        KeyPairElement element = cache.getIfPresent(id);
        if (element == null) {
            return null;
        }
        try {
            PublicKey publicKey = RsaUtil.decodeBase64PublicKey(element.getPublicKey());
            PrivateKey privateKey = RsaUtil.decodeBase64PrivateKey(element.getPrivateKey());
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            log.error("!", e);
        }
        return null;
    }

    /**
     * 删除密钥对
     * @param id 密钥对ID
     */
    public void remove(String id) {
        Optional.ofNullable(id).ifPresent(cache::invalidate);
    }

    // ==============================InnerClass=======================================
    @SuppressWarnings("serial")
    @Data
    public static class KeyPairElement implements Serializable {
        private String privateKey;
        private String publicKey;
    }
}
