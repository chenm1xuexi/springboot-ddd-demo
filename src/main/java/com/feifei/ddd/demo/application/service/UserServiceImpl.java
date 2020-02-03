package com.feifei.ddd.demo.application.service;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户应用服务实现类
 * <p>
 * 应用服务对各个服务进行编排转发，以及调用领域服务，仓储来实现具体功能
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Either<Seq<ApiError>, String> create(UserCreate request) {
        return User.create(request)
                .map(repository::save)
                .map(User::getId);

    }
}
