package yyl.demo.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.github.relucent.base.common.crypto.CipherUtil;
import com.github.relucent.base.common.crypto.CryptoException;
import com.github.relucent.base.common.crypto.ProviderFactory;

/**
 * RSA 加密工具类
 */
public class RsaUtil {

    private static final String RSA = "RSA";
    private static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";

    /**
     * 获得随机密钥对
     * @return 密钥对
     */
    public static KeyPair generateKeyPait() {
        KeyPairGenerator keyPairGenerator;
        try {
            Provider provider = ProviderFactory.getProvider();
            keyPairGenerator = KeyPairGenerator.getInstance(RSA, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 加密数据
     * @param input 被解密的数据
     * @param key 密钥(私钥或公钥)
     * @return 解密后的数据
     */
    public static byte[] encrypt(byte[] input, Key key) {
        try {
            Cipher cipher = getCipher();
            return doFinal(input, cipher, Cipher.ENCRYPT_MODE, key);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 加密数据，返回Base64字符串
     * @param input 被加密的数据
     * @param key 密钥(私钥或公钥)
     * @return 加密后的数据(Base64字符串)
     */
    public static String encryptBase64(String input, Key key) {
        return Base64.encodeBase64String(encrypt(input.getBytes(StandardCharsets.UTF_8), key));
    }

    /**
     * 解密数据
     * @param input 被解密的数据
     * @param key 密钥(私钥或公钥)
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] input, Key key) {
        try {
            Cipher cipher = getCipher();
            return doFinal(input, cipher, Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 解密数据，返回Base64字符串
     * @param input 被解密的数据(Base64字符串)
     * @param key 密钥(私钥或公钥)
     * @return 解密后的数据
     */
    public static String decryptBase64(String input, Key key) {
        return new String(decrypt(Base64.decodeBase64(input), key), StandardCharsets.UTF_8);
    }

    /**
     * 编码秘钥（Base64）
     * @param key 密钥(私钥或公钥)
     * @return Base64 字符串
     */
    public static String encodeBase64String(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 解码秘钥（私钥）
     * @param keyEncoded 密钥字符串(Base64字符串)
     * @return 密钥(私钥)
     */
    public static PrivateKey decodeBase64PrivateKey(String keyEncoded) {
        try {
            byte[] data = Base64.decodeBase64(keyEncoded);
            KeySpec keySpec = new PKCS8EncodedKeySpec(data);
            Provider provider = ProviderFactory.getProvider();
            KeyFactory factory = KeyFactory.getInstance(RSA, provider);
            return factory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 解码秘钥（公钥）
     * @param keyEncoded 密钥字符串(Base64字符串)
     * @return 密钥(公钥)
     */
    public static PublicKey decodeBase64PublicKey(String keyEncoded) {
        byte[] data = Base64.decodeBase64(keyEncoded);
        try {
            KeySpec keySpec = new X509EncodedKeySpec(data);
            Provider provider = ProviderFactory.getProvider();
            KeyFactory factory = KeyFactory.getInstance(RSA, provider);
            return factory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 获得加密算法对象
     * @return 加密算法对象
     */
    private static Cipher getCipher() {
        return CipherUtil.createCipher(RSA_ECB_PKCS1);
    }

    /**
     * 加密或解密数据
     * @param input 需要加密或解密的数据
     * @param cipher 加密算法对象
     * @param key 密钥(私钥或公钥)
     * @param opmode 模式(Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE)
     * @return 加密或解密后的数据
     * @throws Exception 出现异常时候抛出
     */
    private static byte[] doFinal(byte[] input, Cipher cipher, int opmode, Key key) throws Exception {
        // 始化
        cipher.init(opmode, key);
        // 加密解密
        return cipher.doFinal(input);
    }
}