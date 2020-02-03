package com.feifei.ddd.demo.application.service;


import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 *
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImplTest {

    UserService userService;

    @Mock
    UserRepository repository;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(repository);
    }

    @Test
    public void createShouldReturnRight() throws Exception {
        val request = new UserCreate("Gibson", "123456");
        val data = User.create(request).get();
        // 因为mock了一个仓储，所以需要mock一个假的数据
        when(repository.save(any(User.class))).thenReturn(data);
        // 创建一个请求
        val result = userService.create(request);
        // 断言结果是否正确
        Assertions.assertThat(result).isInstanceOf(Either.Right.class);
        Assertions.assertThat(result.get()).isEqualTo(data.getId());
    }

    @Test
    public void createShouldReturnLeft() {
        val request = new UserCreate("Gibson", "12345");

        val result = userService.create(request);
        // 断言结果是否正确
        Assertions.assertThat(result).isInstanceOf(Either.Left.class);
        Assertions.assertThat(result.getLeft().head().msg).isEqualTo("密码不足6位");
    }
}

