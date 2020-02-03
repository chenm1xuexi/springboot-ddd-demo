package com.feifei.ddd.demo.application.service;


import com.feifei.ddd.demo.domain.user.UserDomainService;
import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.tool.IdWorker;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import java.util.Optional;

import static io.vavr.API.None;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 应用服务层测试用例
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

    @Mock
    UserDomainService userDomainService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(repository, userDomainService);
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

    @Test
    public void getInfoShouldReturnSome() throws Exception {
        // 涉及到需要查询持久层的数据库，所以这里需要mock一下，仓储调用逻辑
        User user = new User();
        user.setId(IdWorker.getId());
        user.setUsername("Gibson");
        when(repository.getById(anyString())).thenReturn(Optional.of(user));
        val info = userService.getInfo(user.getId());
        Assertions.assertThat(info).isInstanceOf(Option.Some.class);
        Assertions.assertThat(info.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void getInfoShouldReturnNone() throws Exception {
        when(repository.getById(anyString())).thenReturn(Optional.empty());
        val info = userService.getInfo(IdWorker.getId());
        Assertions.assertThat(info).isInstanceOf(Option.None.class);
    }

    @Test
    public void editShouldReturnSome() throws Exception {

        val dto = new UserEditDTO("Gibson", "54321");

        val user = new User();
        user.setId(IdWorker.getId());
        user.setUsername("Gibson");
        // when(repository.edit(any(User.class))).thenReturn(user);
        when(userDomainService.edit(anyString(), any(UserEditDTO.class))).thenReturn(Option.some(user));
        val result = userService.edit("1", dto);
        Assertions.assertThat(result).isInstanceOf(Option.Some.class);
        Assertions.assertThat(result.get().get().getUsername()).isEqualTo("Gibson");
    }

    @Test
    public void editShouldReturnNone() throws Exception {
        val dto = new UserEditDTO("Gibson", "54321");
        when(userDomainService.edit(anyString(), any(UserEditDTO.class))).thenReturn(None());
        val result = userService.edit("1", dto);
        Assertions.assertThat(result).isInstanceOf(Option.None.class);
    }
}

