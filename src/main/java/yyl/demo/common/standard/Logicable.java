package yyl.demo.common.standard;

public interface Logicable {
    /**
     * 获得版本号
     * @return 版本号
     */
    Long getVersion();

    /**
     * 设置版本号
     * @param version 版本号
     */
    void setVersion(Long version);

    /**
     * 获得删除标记
     * @return 删除标记
     */
    Integer getDeleted();

    /**
     * 设置删除标记
     * @param deleted 删除标记
     */
    void setDeleted(Integer deleted);
}