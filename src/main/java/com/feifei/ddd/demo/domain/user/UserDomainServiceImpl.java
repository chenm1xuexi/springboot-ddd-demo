package com.feifei.ddd.demo.domain.user;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.feifei.ddd.demo.domain.user.entity.User.validatePassword;
import static io.vavr.API.Left;


/**
 * 用户领域服务实现类
 *
 * @author xiaofeifei
 * @date 2020-02-04
 * @since
 */
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDomainServiceImpl implements UserDomainService {

    UserRepository repository;

    @Override
    public Option<Either<Seq<ApiError>, User>> edit(String id, UserEditDTO request) {
        // 首先通过id来查询用户信息是否存在
        return Option.ofOptional(repository.getById(id))
                .map(some -> {
                    // 如果存在该用户，则校验需要修改的用户名是否存在，存在则报错
                    val isExist = repository.getByUsername(request.getUsername()).isDefined();
                    if (isExist) {
                        return Left(API.Seq(ApiError.create(3, "用户名已存在")));
                    }
                    // 不存在则校验密码是否正确,正确则进行赋值
                    return validatePassword(request.getPassword()).map(p -> {
                        some.setUsername(request.getUsername());
                        some.setPassword(request.getPassword());
                        some.setUpdateAt(LocalDateTime.now());
                        // 具体的存储可交由应用服务层来完成，这里只返回需要存储的实体对象
                        return some;
                    });
                });
    }
}
