package yyl.demo.kit;

import yyl.demo.entity.PermissionEntity;
import yyl.demo.model.dto.PermissionDTO;
import yyl.demo.model.ro.PermissionRO;
import yyl.demo.model.vo.PermissionNodeVO;
import yyl.demo.model.vo.PermissionVO;

public class PermissionKit {

    public static void copyProperties(PermissionDTO dto, PermissionEntity entity) {
        entity.setId(dto.getId());
        entity.setParentId(dto.getParentId());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        entity.setCode(dto.getCode());
        entity.setPath(dto.getPath());
        entity.setIcon(dto.getIcon());
        entity.setOrdinal(dto.getOrdinal());
    }

    public static PermissionVO toVO(PermissionEntity entity) {
        PermissionVO vo = new PermissionVO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        vo.setType(entity.getType());
        vo.setCode(entity.getCode());
        vo.setPath(entity.getPath());
        vo.setIcon(entity.getIcon());
        vo.setOrdinal(entity.getOrdinal());
        return vo;
    }

    public static PermissionRO toRO(PermissionEntity entity) {
        PermissionRO vo = new PermissionRO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        vo.setType(entity.getType());
        vo.setCode(entity.getCode());
        vo.setPath(entity.getPath());
        vo.setIcon(entity.getIcon());
        vo.setOrdinal(entity.getOrdinal());
        return vo;
    }

    public static PermissionNodeVO toNodeVO(PermissionEntity entity) {
        PermissionNodeVO vo = new PermissionNodeVO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setName(entity.getName());
        vo.setType(entity.getType());
        vo.setCode(entity.getCode());
        vo.setPath(entity.getPath());
        vo.setIcon(entity.getIcon());
        vo.setRemark(entity.getRemark());
        vo.setOrdinal(entity.getOrdinal());
        return vo;
    }
}
