package yyl.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import yyl.demo.common.env.BannerUtil;

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
		ConfigurableApplicationContext context = SpringApplication.run(DemoWebApplication.class, args);
		BannerUtil.printEnvironment(context);
		log.info("[Startup Success]");
	}
}
