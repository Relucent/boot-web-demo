package yyl.demo.common.standard;

import java.util.Date;

/**
 * 可审计对象(包含创建/修改时间信息)
 */
public interface Auditable {

    /**
     * 获得创建者
     * @return 创建者ID
     */
    String getCreatedBy();

    /**
     * 获得创建时间
     * @return 创建时间
     */
    Date getCreatedAt();

    /**
     * 获得修改者
     * @return 修改者ID
     */
    String getUpdatedBy();

    /**
     * 获得修改时间
     * @return 修改时间
     */
    Date getUpdatedAt();

    /**
     * 设置创建者
     * @param 创建者ID
     */
    void setCreatedBy(String createdBy);

    /**
     * 设置创建时间
     * @param 创建时间
     */
    void setCreatedAt(Date createdAt);

    /**
     * 设置修改者
     * @param 修改者ID
     */
    void setUpdatedBy(String updatedBy);

    /**
     * 设置修改时间
     * @param 修改时间
     */
    void setUpdatedAt(Date updatedAt);
}
