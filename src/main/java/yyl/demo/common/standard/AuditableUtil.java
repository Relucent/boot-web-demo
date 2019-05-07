package yyl.demo.common.standard;

import java.util.Date;

import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.util.time.DateUtil;

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
        record.setCreatedBy(principal.getUserId());
        record.setCreatedDate(now);
        record.setLastModifiedBy(principal.getUserId());
        record.setLastModifiedDate(now);
    }

    /**
     * 设置修改信息
     * @param record 记录对象
     * @param principal 登录人
     */
    public static void setLastModified(Auditable record, Principal principal) {
        Date now = DateUtil.now();
        record.setLastModifiedBy(principal.getUserId());
        record.setLastModifiedDate(now);
    }
}
