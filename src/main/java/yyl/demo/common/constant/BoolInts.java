package yyl.demo.common.constant;

import java.util.Objects;

import com.github.relucent.base.common.convert.ConvertUtil;

/** 布尔数值常量 */
public class BoolInts {
    /** 真 */
    public static final int TRUE = 1;

    /** 假 */
    public static final int FALSE = 0;

    /**
     * 规范化布尔数值
     * @param value 布尔数值
     * @return 布尔数值(0 或者 1)
     */
    public static int normalize(Integer value) {
        return Objects.equals((Integer) TRUE, value) ? TRUE : FALSE;
    }

    /**
     * 规范化布尔数值
     * @param value 布尔数值字符串
     * @return 布尔数值(0 或者 1)
     */
    public static int normalize(String value) {
        return ConvertUtil.toBoolean(value, Boolean.FALSE) ? TRUE : FALSE;
    }
}
