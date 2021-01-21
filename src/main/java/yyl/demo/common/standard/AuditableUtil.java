package yyl.demo.common.standard;

import java.util.Date;

import com.github.relucent.base.common.time.DateUtil;

import yyl.demo.common.security.UserPrincipal;

/**
 * 可审计对象工具类
 */
public class AuditableUtil {
    /**
     * 设置创建信息
     * @param record 记录对象
     * @param principal 登录人
     */
    public static void setCreated(Auditable record, UserPrincipal principal) {
        Date now = DateUtil.now();
        if (principal != null) {
            record.setCreatedBy(principal.getId());
            record.setModifiedBy(principal.getId());
        }
        record.setCreatedDate(now);
        record.setModifiedDate(now);
    }

    /**
     * 设置修改信息
     * @param record 记录对象
     * @param principal 登录人
     */
    public static void setUpdated(Auditable record, UserPrincipal principal) {
        Date now = DateUtil.now();
        if (principal != null) {
            record.setModifiedBy(principal.getId());
        }
        record.setModifiedDate(now);
    }
}
