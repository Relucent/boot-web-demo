package yyl.demo.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.relucent.base.util.convert.ConvertUtil;
import com.github.relucent.base.util.net.NetworkHelper;

/**
 * 基础常量类
 * @author YYL
 */
public class BaseConstants {

    /** ID常量 */
    public static class Ids {

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
            String[] mac = NetworkHelper.getMacAddress();
            if (mac != null && mac.length != 0) {
                seed = Arrays.toString(mac);
            } else {
                seed = UUID.randomUUID().toString();
            }
            CURRENT_SERVER_ID = DigestUtils.md5Hex(seed);
        }
    }

    /** 符号常量 */
    public static class Symbols {

        /** 空字符串 */
        public static final String EMPTY = "";

        /** 非标记 */
        public static final String NON = "!";

        /** 默认的路径分隔符 */
        public static final String SEPARATOR = "/";

        /** 逗号 */
        public static final String COMMA = ",";

        /** 点 */
        public static final String DOT = ".";

        /** 哈希符号 */
        public static final String HASH = "#";

        /** 密码占位符 */
        public static final String PASSWORD_PLACEHOLDER = "●●●●●●";

        /** 重定向前缀 */
        public static final String REDIRECT_PREFIX = "redirect:";
    }

    /** 返回结果枚举 */
    public static enum ResultStatus {
        OK(0), ERROR(1);
        public final Integer VALUE;

        private ResultStatus(Integer value) {
            this.VALUE = value;
        }
    }

    /** 布尔数值常量 */
    public static class BoolInts {

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

    /** 功能权限类型 */
    public static class PermissionType {
        /** 模块 */
        public static final Integer MODULE = 1;
        /** 菜单 */
        public static final Integer MENU = 2;
        /** 按钮 */
        public static final Integer BUTTON = 3;
    }
}
