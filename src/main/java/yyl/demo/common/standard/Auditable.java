package yyl.demo.common.standard;

import java.util.Date;

/**
 * 可审计对象(包含创建/修改时间信息)
 */
public interface Auditable {
    /**
     * 获得创建者
     * @param 创建者ID
     */
    String getCreatedBy();

    /**
     * 设置创建者
     * @param createdBy 创建者ID
     */
    void setCreatedBy(String createdBy);

    /**
     * 获得创建时间
     * @return 创建时间
     */
    Date getCreatedAt();

    /**
     * 设置创建时间
     * @param createdAt 创建时间
     */
    void setCreatedAt(Date createdAt);

    /**
     * 获得最后的修改者
     * @param 修改者ID
     */
    String getUpdatedBy();

    /**
     * 设置最后的修改者
     * @param updatedBy 修改者ID
     */
    void setUpdatedBy(String updatedBy);

    /**
     * 获得最后修改时间
     * @return 获得最后的修改时间
     */
    Date getUpdatedAt();

    /**
     * 设置最后修改时间
     * @param updatedAt 获得最后的修改时间
     */
    void setUpdatedAt(Date updatedAt);
}
