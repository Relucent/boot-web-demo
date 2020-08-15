package yyl.demo.common.standard;

import java.util.Date;

import com.github.relucent.base.common.time.DateUtil;
import com.github.relucent.base.plugin.security.Principal;

/**
 * 可审计对象工具类
 * @author Administrator
 */
public class AuditableUtil {
    /**
     * 设置创建信息
     * @param record 记录对象
     * @param principal 登录人
     */
    public static void setCreated(Auditable record, Principal principal) {
        Date now = DateUtil.now();
        if (principal != null) {
            record.setCreatedBy(principal.getUserId());
            record.setModifiedBy(principal.getUserId());
        }
        record.setCreatedDate(now);
        record.setModifiedDate(now);
    }

    /**
     * 设置修改信息
     * @param record 记录对象
     * @param principal 登录人
     */
    public static void setUpdated(Auditable record, Principal principal) {
        Date now = DateUtil.now();
        if (principal != null) {
            record.setModifiedBy(principal.getUserId());
        }
        record.setModifiedDate(now);
    }
}
