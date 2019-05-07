package yyl.demo.common.standard;

import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.util.lang.DateUtil;

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
        record.setCreatedBy(principal.getUserId()); 
        record.setCreatedAt(DateUtil.now());
    }

    /**
     * 设置修改信息
     * @param record 记录对象
     * @param principal 登录人
     */
    public static void setUpdated(Auditable record, Principal principal) {
        record.setUpdatedBy(principal.getUserId());
        record.setUpdatedAt(DateUtil.now());
    }
}
