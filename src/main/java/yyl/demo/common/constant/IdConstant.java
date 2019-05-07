package yyl.demo.common.constant;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.relucent.base.common.net.NetworkUtil;

/**
 * ID常量
 */
public class IdConstant {

    /** 数据中，代表无关联的ID */
    public static final String NONX = "";

    /** 系统内置管理员的ID */
    public static final String ADMIN_ID = "_admin";

    /** 根节点ID标记 */
    public static final String ROOT_ID = "0";

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

    /**
     * 是否 None Id
     * @param id ID
     * @return 如果输入是 null 或者 none(0)，则返回true，否则返回false
     */
    public static boolean isNonId(String id) {
        return StringUtils.isEmpty(id) || NONX.equals(id);
    }

    IdConstant() {
    }
}
