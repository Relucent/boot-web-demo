package yyl.demo.common.standard;

import java.util.Date;

/**
 * 可审计对象(包含创建/修改时间信息)
 */
public interface Auditable {
    /**
     * 获得创建者
     * @param 创建者
     */
    String getCreatedBy();

    /**
     * 设置创建者
     * @param createdBy 创建者
     */
    void setCreatedBy(String createdBy);

    /**
     * 获得创建时间
     * @return 创建时间
     */
    Date getCreatedDate();

    /**
     * 设置创建时间
     * @param createdDate 创建时间
     */
    void setCreatedDate(Date createdDate);

    /**
     * 获得最后的修改者
     * @param 修改者
     */
    String getModifiedBy();

    /**
     * 设置最后的修改者
     * @param modifiedBy 修改者
     */
    void setModifiedBy(String modifiedBy);

    /**
     * 获得最后修改时间
     * @return 获得最后的修改时间
     */
    Date getModifiedDate();

    /**
     * 设置最后修改时间
     * @param modifiedDate 最后的修改时间
     */
    void setModifiedDate(Date modifiedDate);
}
