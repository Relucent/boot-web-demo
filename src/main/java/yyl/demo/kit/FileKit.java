package yyl.demo.kit;

import yyl.demo.entity.FileEntity;
import yyl.demo.model.vo.FileVO;

public class FileKit {
    public static FileVO toVO(FileEntity entity) {
        FileVO vo = new FileVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setExtension(entity.getExtension());
        vo.setContentType(entity.getContentType());
        vo.setLength(entity.getLength());
        vo.setPath(entity.getPath());
        return vo;
    }
}
