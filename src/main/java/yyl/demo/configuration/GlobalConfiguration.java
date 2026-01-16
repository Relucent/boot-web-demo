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
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.relucent.base.common.collection.Listx;
import com.github.relucent.base.common.collection.Mapx;
import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.plugin.jackson.JacksonConvertUtil;
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

        ObjectMapper om = new ObjectMapper();

        // JSON 解析特性
        om.enable(JsonParser.Feature.ALLOW_COMMENTS); // 支持注释
        om.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES); // 支持字段名不加引号
        om.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES); // 支持单引号字符串

        // 反序列化特性
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 忽略未知字段
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); // 单值解析为数组
        om.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE); // 枚举未知值用默认
        om.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE); // 禁止时区自动调整

        // 序列化特性
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS); // 空 Bean 输出 {}
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 日期输出 ISO 字符串
        om.getSerializationConfig().without(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);// 按类定义顺序

        // 时区设置
        // om.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // 注册模块
        om.registerModules(new SimpleModule() //
                .addSerializer(BigDecimal.class, BigDecimalPowerSerializer.INSTANCE)//
                .addDeserializer(BigDecimal.class, BigDecimalPowerDeserializer.INSTANCE)//
                .addSerializer(Date.class, DatePowerSerializer.INSTANCE)//
                .addDeserializer(Date.class, DatePowerDeserializer.INSTANCE)//
                .addDeserializer(Mapx.class, JacksonConvertUtil.MAP_DESERIALIZER)//
                .addDeserializer(Listx.class, JacksonConvertUtil.LIST_DESERIALIZER)//
        );
        om.findAndRegisterModules();// JSR310

        JsonUtil.setHandler(new JacksonHandler(om));
    }
}
