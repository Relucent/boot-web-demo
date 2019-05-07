package yyl.demo.common.standard;

import java.util.Date;

import com.github.relucent.base.common.time.DateUtil;

/**
 * 可审计对象工具类
 */
public class AuditableUtil {

    /**
     * 设置创建信息
     * @param entity 记录对象
     * @param identity 操作人ID
     */
    public static void setCreated(Auditable entity, String identity) {
        Date now = DateUtil.now();
        entity.setCreatedBy(identity);
        entity.setUpdatedBy(identity);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
    }

    /**
     * 设置修改信息
     * @param entity 记录对象
     * @param identity 操作人ID
     */
    public static void setUpdated(Auditable entity, String identity) {
        Date now = DateUtil.now();
        entity.setUpdatedBy(identity);
        entity.setUpdatedAt(now);
    }

    /**
     * 清空审计信息
     * @param entity 记录对象
     */
    public static void clean(Auditable entity) {
        entity.setCreatedBy(null);
        entity.setCreatedAt(null);
        entity.setUpdatedBy(null);
        entity.setUpdatedAt(null);
    }
}
