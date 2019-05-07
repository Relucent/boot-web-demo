package helper;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class StringEncryptorExample {
    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());
        encryptor.setPassword("password");
        String encrypted = encryptor.encrypt("root");
        System.out.println("encrypted=" + encrypted);
        String decrypted = encryptor.decrypt(encrypted);
        System.out.println("decrypted=" + decrypted);
    }
}
