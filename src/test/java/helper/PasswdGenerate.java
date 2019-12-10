package helper;

import java.util.Scanner;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswdGenerate {
    public static void main(String[] args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println("[Encryption]");
        try (Scanner reader = new Scanner(System.in)) {
            for (;;) {
                System.out.print("Please enter text:");
                String rawPassword = reader.nextLine();
                System.out.println(encoder.encode(rawPassword));
                if ("exit".equals(rawPassword)) {
                    return;
                }
            }
        }
    }
}
