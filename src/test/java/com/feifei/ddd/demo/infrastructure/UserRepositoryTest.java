package com.feifei.ddd.demo.infrastructure;

import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.jpa.mapper.UserMapper;
import com.feifei.ddd.demo.infrastructure.jpa.po.TbUser;
import com.feifei.ddd.demo.infrastructure.tool.IdWorker;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@RunWith(SpringRunner.class)
//@DataJpaTest // 采用此注解无法将@component注解的实例注册到spring容器中，采用@springbootTest进行代替
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    UserMapper userMapper;

    @Test
    public void saveShouldReturnInfo() {
        User user = new User();
        user.setId(IdWorker.getId());
        user.setUsername("Gibson");
        user.setPassword("123456");
        val now = LocalDateTime.now();
        user.setCreateAt(now);
        user.setUpdateAt(now);
        repository.save(user);
        val tbUser = userMapper.selectById(user.getId());
        Assertions.assertThat(tbUser).isNotNull();
        Assertions.assertThat(tbUser).isInstanceOf(TbUser.class);
        Assertions.assertThat(tbUser.getId()).isEqualTo(user.getId());
        System.out.println("tbUser = " + tbUser);
    }

    @Test
    public void findByUsernameShouldReturnSome() {
        val user = new TbUser();
        val id = IdWorker.getId();
        user.setUsername(id);
        user.setId(id);
        user.setPassword("123456");
        val now = LocalDateTime.now();
        user.setCreateAt(now);
        user.setUpdateAt(now);

        userMapper.insert(user);
        val result = repository.getByUsername(id);
        Assertions.assertThat(result).isInstanceOf(Option.Some.class);
        Assertions.assertThat(result.get().getUsername()).isEqualTo(id);
    }

    @Test
    public void findByUsernameShouldReturnNone() {
        val result = repository.getByUsername("1");
        Assertions.assertThat(result).isInstanceOf(Option.None.class);
    }
}