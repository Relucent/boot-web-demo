package yyl.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序入口类
 * @author YYL
 */
@SpringBootApplication
public class DemoWebApplication {

    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoWebApplication.class);

    /**
     * 应用入口
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoWebApplication.class, args);
        LOGGER.info("[Startup Success]");
    }
}
