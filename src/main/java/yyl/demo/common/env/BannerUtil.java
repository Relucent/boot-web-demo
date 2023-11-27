package yyl.demo.common.env;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.github.relucent.base.common.net.NetworkUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BannerUtil {

	public static void printEnvironment(ApplicationContext application) {

		Environment environment = application.getEnvironment();

		String applicationName = environment.getProperty("spring.application.name");
		String hostAddress = NetworkUtil.getHostAddress();
		String serverPort = environment.getProperty("server.port");
		String servletContextPath = StringUtils.removeEnd(environment.getProperty("server.servlet.context-path"), "/");

		log.info("\n"//
				+ "----------------------------------------------------------\n" //
				+ "\t[Startup Success]\n" //
				+ "\tApplication: {} [Startup Success]\n" //
				+ "\tSwagger: http://localhost:{}{}/doc.html\n" //
				+ "\t         http://{}:{}{}/doc.html\n" //
				+ "----------------------------------------------------------", //
				applicationName, //
				hostAddress, //
				serverPort, //
				servletContextPath, //
				serverPort, //
				servletContextPath);
	}
}
