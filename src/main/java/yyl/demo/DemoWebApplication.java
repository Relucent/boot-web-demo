package yyl.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 应用程序入口类
 * @author YYL
 */
@Slf4j
@SpringBootApplication
public class DemoWebApplication {

    /**
     * 应用入口
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoWebApplication.class, args);
        log.info("[Startup Success]");
    }
}
