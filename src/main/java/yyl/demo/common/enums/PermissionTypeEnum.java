package yyl.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 权限类型
 */
@Accessors(fluent = true)
@AllArgsConstructor
@Getter
public enum PermissionTypeEnum {

    /** 模块 */
    MODULE(1),
    /** 目录 */
    FOLDER(2),
    /** 菜单 */
    MENU(3),
    /** 按钮 */
    BUTTON(4);

    private final Integer value;

    /**
     * 判断值与枚举值是否一致
     * @param value 枚举值
     * @return 如果一致则返回true，否则返回false
     */
    public boolean is(Integer value) {
        return equals(of(value, null));
    }

    /**
     * 转化获得角色类型(枚举对象)
     * @param value 枚举值
     * @param defaultValue 默认枚举
     * @return 枚举对象
     */
    public static PermissionTypeEnum of(Integer value, PermissionTypeEnum defaultValue) {
        if (value != null) {
            for (PermissionTypeEnum e : PermissionTypeEnum.values()) {
                if (e.value().equals(value)) {
                    return e;
                }
            }
        }
        return defaultValue;
    }
}