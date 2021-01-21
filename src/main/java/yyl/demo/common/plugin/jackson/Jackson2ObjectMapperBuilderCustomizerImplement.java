package yyl.demo.common.plugin.jackson;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.relucent.base.plugin.jackson.databind.BigDecimalPowerDeserializer;
import com.github.relucent.base.plugin.jackson.databind.BigDecimalPowerSerializer;
import com.github.relucent.base.plugin.jackson.databind.DatePowerDeserializer;
import com.github.relucent.base.plugin.jackson.databind.DatePowerSerializer;

public class Jackson2ObjectMapperBuilderCustomizerImplement implements Jackson2ObjectMapperBuilderCustomizer {
    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        SimpleModule module = new SimpleModule();
        // 将大数字转换为 String 类型
        module.addSerializer(BigDecimal.class, BigDecimalPowerSerializer.INSTANCE);
        module.addDeserializer(BigDecimal.class, BigDecimalPowerDeserializer.INSTANCE);
        // 日期序列化与反序列化
        module.addSerializer(Date.class, DatePowerSerializer.INSTANCE);
        module.addDeserializer(Date.class, DatePowerDeserializer.INSTANCE);
        builder.modules(module);
    }
}
