package yyl.demo.dao.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.User;
import yyl.demo.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Before
    public void before() throws Exception {
        userMapper.insert(ofUser("10000", "U1", "~", "Jeff Bezos", "/", 1));
        userMapper.insert(ofUser("20000", "U2", "~", "Bill Gates", "/", 1));
        userMapper.insert(ofUser("30000", "U3", "~", "Warren Buffett", "/", 1));
        userMapper.insert(ofUser("40000", "U4", "~", "Bernard Arnault", "/", 1));
        userMapper.insert(ofUser("50000", "U5", "~", "Mark Zuckerberg", "/", 1));
    }

    @Test
    public void testFindAll() throws Exception {
        for (User record : userMapper.selectList(Wrappers.emptyWrapper())) {
            System.out.println(record);
        }
    }

    private User ofUser(String id, String username, String password, String name, String remark, Integer enabled) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setRemark(remark);
        user.setEnabled(enabled);
        return user;
    }

}
