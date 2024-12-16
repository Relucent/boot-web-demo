package yyl.demo.configuration;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.plugin.jackson.JacksonHandler;
import com.github.relucent.base.plugin.jackson.databind.BigDecimalPowerDeserializer;
import com.github.relucent.base.plugin.jackson.databind.BigDecimalPowerSerializer;
import com.github.relucent.base.plugin.jackson.databind.DatePowerDeserializer;
import com.github.relucent.base.plugin.jackson.databind.DatePowerSerializer;

import yyl.demo.common.jackson.Jackson2ObjectMapperBuilderCustomizerImplement;

/**
 * 项目公用配置
 * @author YYL
 */
@Configuration
public class GlobalConfiguration {

	/**
	 * _Jackson 定制器
	 * @return _Jackson 定制器
	 */
	@Primary
	@Bean
	Jackson2ObjectMapperBuilderCustomizer Jackson2ObjectMapperBuilderCustomizer() {
		return new Jackson2ObjectMapperBuilderCustomizerImplement();
	}

	@PostConstruct
	public void initialize() {
		JsonUtil.setHandler(new JacksonHandler(new ObjectMapper()//
				.enable(JsonParser.Feature.ALLOW_COMMENTS)//
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)//
				.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)//
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)//
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)//
				.registerModules(new SimpleModule() //
						.addSerializer(BigDecimal.class, BigDecimalPowerSerializer.INSTANCE)//
						.addDeserializer(BigDecimal.class, BigDecimalPowerDeserializer.INSTANCE)//
						.addSerializer(Date.class, DatePowerSerializer.INSTANCE)//
						.addDeserializer(Date.class, DatePowerDeserializer.INSTANCE))//
				.findAndRegisterModules())//
		);
	}
}
