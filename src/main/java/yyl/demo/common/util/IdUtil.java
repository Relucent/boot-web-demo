package yyl.demo.common.util;

import com.github.relucent.base.common.identifier.TimeId;
import com.github.relucent.base.common.identifier.UUID32;

/**
 * 序列ID生成器<br>
 */
public class IdUtil {

    private IdUtil() {
    }

    /**
     * 获得18位时间序列ID
     * @return 18位时间序列ID
     */
    public static String timeId() {
        return TimeId.generate().toString();
    }

    /**
     * 生成32位UUID
     * @return UUID
     */
    public static String uuid32() {
        return UUID32.randomUUID().toString();
    }
}
