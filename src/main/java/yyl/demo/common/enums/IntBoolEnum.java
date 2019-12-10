package yyl.demo.common.enums;

import com.github.relucent.base.common.convert.ConvertUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 布尔数值常量枚举
 */
@Accessors(fluent = true)
@AllArgsConstructor
@Getter
public enum IntBoolEnum {

    /** 是 */
    Y(1),
    /** 否 */
    N(0);

    private final Integer value;

    public boolean is(Object value) {
        return equals(of(value, null));
    }

    public static IntBoolEnum of(Object value) {
        return of(value, IntBoolEnum.N);
    }

    public static IntBoolEnum of(Object value, IntBoolEnum defaultValue) {
        if (value instanceof IntBoolEnum) {
            return (IntBoolEnum) value;
        }
        Boolean bool = ConvertUtil.toBoolean(value, null);
        if (bool == null) {
            return defaultValue;
        }
        return bool.booleanValue() ? Y : N;
    }
}
