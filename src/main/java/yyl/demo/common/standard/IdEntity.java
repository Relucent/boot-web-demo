package yyl.demo.common.standard;

import java.io.Serializable;

/**
 * 默认的实体接口类（为保证统一要求项目的实体类都实现该接口）
 */
public interface IdEntity extends Idable, Logicable, Auditable, Serializable {
}
