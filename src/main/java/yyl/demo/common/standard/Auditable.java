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
    Date getCreatedDate();

    /**
     * 设置创建时间
     * @param createdDate 创建时间
     */
    void setCreatedDate(Date createdDate);

    /**
     * 获得最后的修改者
     * @param 修改者ID
     */
    String getLastModifiedBy();

    /**
     * 设置最后的修改者
     * @param lastModifiedBy 修改者ID
     */
    void setLastModifiedBy(String lastModifiedBy);

    /**
     * 获得最后修改时间
     * @return 获得最后的修改时间
     */
    Date getLastModifiedDate();

    /**
     * 设置最后修改时间
     * @param lastModifiedDate 获得最后的修改时间
     */
    void setLastModifiedDate(Date lastModifiedDate);
}
