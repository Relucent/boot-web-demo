package helper;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class StringEncryptorExample {
    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("password");
        String encrypted = encryptor.encrypt("root");
        System.out.println("encrypted=" + encrypted);
        String decrypted = encryptor.decrypt(encrypted);
        System.out.println("decrypted=" + decrypted);
    }
}
