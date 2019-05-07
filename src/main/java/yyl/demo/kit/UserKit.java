package yyl.demo.kit;

import yyl.demo.entity.UserEntity;
import yyl.demo.model.dto.UserDTO;
import yyl.demo.model.ro.UserRO;
import yyl.demo.model.vo.UserVO;

public class UserKit {

    public static void copyProperties(UserDTO dto, UserEntity entity) {
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setRealname(dto.getRealname());
        entity.setSex(dto.getSex());
        entity.setPhone(dto.getPhone());
        entity.setRemark(dto.getRemark());
        entity.setEnabled(dto.getEnabled());
    }

    public static UserVO toVO(UserEntity entity) {
        UserVO vo = new UserVO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getUsername());
        vo.setOrganizationId(entity.getOrganizationId());
        vo.setRealname(entity.getRealname());
        vo.setSex(entity.getSex());
        vo.setPhone(entity.getPhone());
        vo.setRemark(entity.getRemark());
        vo.setEnabled(entity.getEnabled());
        return vo;
    }

    public static UserRO toRO(UserEntity entity) {
        UserRO vo = new UserRO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getUsername());
        vo.setOrganizationId(entity.getOrganizationId());
        vo.setRealname(entity.getRealname());
        vo.setSex(entity.getSex());
        vo.setPhone(entity.getPhone());
        vo.setRemark(entity.getRemark());
        vo.setEnabled(entity.getEnabled());
        return vo;
    }
}
