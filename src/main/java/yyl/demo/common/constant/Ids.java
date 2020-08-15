package yyl.demo.common.constant;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.relucent.base.common.net.NetworkUtil;

/** ID常量 */
public class Ids {

    /** 未定义ID标记 */
    public static final String UNDEFINED_ID = "#";

    /** 系统内置管理员的ID */
    public static final String ADMIN_ID = "0";

    /** 根节点ID标记 */
    public static final String ROOT_ID = "0";

    /** 部门树根节点ID标记 */
    public static final String DEPT_ROOT_ID = "";

    /** 数据目录根节点ID标记 */
    public static final String DATA_DIRECTORY_ROOT_ID = "";

    /** 当前系统ID */
    public static final String CURRENT_SERVER_ID;
    static {
        final String seed;
        String[] mac = NetworkUtil.getMacAddress();
        if (mac != null && mac.length != 0) {
            seed = Arrays.toString(mac);
        } else {
            seed = UUID.randomUUID().toString();
        }
        CURRENT_SERVER_ID = DigestUtils.md5Hex(seed);
    }
}
