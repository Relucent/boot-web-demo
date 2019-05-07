package yyl.demo.kit;

import yyl.demo.entity.RoleEntity;
import yyl.demo.model.dto.RoleDTO;
import yyl.demo.model.ro.RoleRO;
import yyl.demo.model.vo.RoleVO;

public class RoleKit {

    public static void copyProperties(RoleDTO dto, RoleEntity entity) {
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
    }

    public static RoleVO toVO(RoleEntity entity) {
        RoleVO vo = new RoleVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public static RoleRO toRO(RoleEntity entity) {
        RoleRO vo = new RoleRO();
        vo.setId(entity.getId());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
