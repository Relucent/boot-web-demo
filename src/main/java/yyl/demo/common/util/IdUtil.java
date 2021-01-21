package yyl.demo.common.util;

import com.github.relucent.base.common.identifier.TimeIdWorker;
import com.github.relucent.base.common.identifier.UUID32;

/**
 * ID 工具类 <br>
 */
public class IdUtil {
    /**
     * 获得32位UUID
     * @return 32位UUID
     */
    public static String uuid32() {
        return UUID32.randomUUID().toString();
    }

    /**
     * 获得时序 ID
     * @return 时序 ID
     */
    public static String timeId() {
        return TimeIdWorker.DEFAULT.nextId() + TimeIdWorker.getIdSuffixFromEnvironment();
    }
}
