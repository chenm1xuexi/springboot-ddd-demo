package com.feifei.ddd.demo.domain.user;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
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

import static io.vavr.API.Option;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * @author xiaofeifei
 * @date 2020-02-04
 * @since
 */
@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDomainServiceImplTest {

    UserDomainService userDomainService;

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() {
        userDomainService = new UserDomainServiceImpl(userRepository);
    }

    @Test
    public void editShouldReturnSomeRight() throws Exception {
        val request = new UserEditDTO();
        request.setUsername("gibson");
        request.setPassword("123456");

        val user = new User();
        user.setUsername("gibson");
        user.setPassword("123456");

        // mock仓储getById和getByUsername
        when(userRepository.getById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.getByUsername(anyString())).thenReturn(Option.none());

        val result = userDomainService.edit("1", request);
        Assertions.assertThat(result).isInstanceOf(Option.Some.class);
        Assertions.assertThat(result.get().get().getUsername()).isEqualTo(request.getUsername());
    }

    @Test
    public void editShouldReturnSomeLeft() throws Exception {
        val request = new UserEditDTO();
        request.setUsername("gibson");
        request.setPassword("123456");

        val user = new User();
        user.setUsername("gibson");
        user.setPassword("123456");
        // mock仓储getById和getByUsername
        when(userRepository.getById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.getByUsername(anyString())).thenReturn(Option(user));
        val result = userDomainService.edit("1", request);
        Assertions.assertThat(result).isInstanceOf(Option.Some.class);
        Assertions.assertThat(result.get().getLeft().head().msg).isEqualTo("用户名已存在");


        when(userRepository.getByUsername(anyString())).thenReturn(Option.none());
        val request1 = new UserEditDTO();
        request1.setUsername("gibson");
        request1.setPassword("12345");
        val result1 = userDomainService.edit("1", request1);
        Assertions.assertThat(result1).isInstanceOf(Option.Some.class);
        Assertions.assertThat(result1.get().getLeft().head().msg).isEqualTo("密码不足6位");

    }

    @Test
    public void editShouldReturnNone() throws Exception {
        val request = new UserEditDTO();
        request.setUsername("gibson");
        request.setPassword("123456");
        when(userRepository.getById(anyString())).thenReturn(Optional.empty());
        val result = userDomainService.edit("1", request);
        Assertions.assertThat(result).isInstanceOf(Option.None.class);
    }




}