package yyl.demo.kit;

import yyl.demo.entity.OrganizationEntity;
import yyl.demo.model.dto.OrganizationDTO;
import yyl.demo.model.ro.OrganizationRO;
import yyl.demo.model.vo.OrganizationNodeVO;
import yyl.demo.model.vo.OrganizationVO;

public class OrganizationKit {

    public static void copyProperties(OrganizationDTO dto, OrganizationEntity entity) {
        entity.setId(dto.getId());
        entity.setParentId(dto.getParentId());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setOrdinal(dto.getOrdinal());
    }

    public static OrganizationVO toVO(OrganizationEntity entity) {
        OrganizationVO vo = new OrganizationVO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        vo.setOrdinal(entity.getOrdinal());
        return vo;
    }

    public static OrganizationRO toRO(OrganizationEntity entity) {
        OrganizationRO vo = new OrganizationRO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        vo.setOrdinal(entity.getOrdinal());
        return vo;
    }

    public static OrganizationNodeVO toNodeVO(OrganizationEntity entity) {
        OrganizationNodeVO vo = new OrganizationNodeVO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        vo.setOrdinal(entity.getOrdinal());
        return vo;
    }
}
