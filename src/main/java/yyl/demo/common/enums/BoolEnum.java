package yyl.demo.common.enums;

import com.github.relucent.base.common.convert.ConvertUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
@Getter
public enum BoolEnum {

    YES("Y", 1), NO("N", 0);

    private final String varchar;
    private final Integer integer;

    public static BoolEnum of(Object value, BoolEnum defaultValue) {
        Boolean bool = ConvertUtil.toBoolean(defaultValue, null);
        if (bool == null) {
            return defaultValue;
        }
        return bool.booleanValue() ? YES : NO;
    }
}
