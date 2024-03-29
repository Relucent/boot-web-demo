package yyl.demo.dao.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.UserEntity;
import yyl.demo.mapper.UserMapper;

@SpringBootTest
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void before() throws Exception {
        userMapper.insert(ofUser("10000", "U1", "~", "Jeff Bezos", "/", 1));
        userMapper.insert(ofUser("20000", "U2", "~", "Bill Gates", "/", 1));
        userMapper.insert(ofUser("30000", "U3", "~", "Warren Buffett", "/", 1));
        userMapper.insert(ofUser("40000", "U4", "~", "Bernard Arnault", "/", 1));
        userMapper.insert(ofUser("50000", "U5", "~", "Mark Zuckerberg", "/", 1));
    }

    @Test
    public void testFindAll() throws Exception {
        for (UserEntity record : userMapper.selectList(Wrappers.emptyWrapper())) {
            System.out.println(record);
        }
    }

    private UserEntity ofUser(String id, String username, String password, String realname, String remark, Integer enabled) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setOrganizationId("");
        user.setUsername(username);
        user.setPassword(password);
        user.setRealname(realname);
        user.setRemark(remark);
        user.setEnabled(enabled);
        return user;
    }

}
