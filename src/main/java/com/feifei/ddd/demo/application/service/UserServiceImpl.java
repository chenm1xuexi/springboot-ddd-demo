package com.feifei.ddd.demo.application.service;

import com.feifei.ddd.demo.domain.user.UserDomainService;
import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.tool.Pagination;
import com.feifei.ddd.demo.interfaces.assembler.UserAssembler;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.data.domain.Page;
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

    UserDomainService userDomainService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Either<Seq<ApiError>, String> create(UserCreate request) {
        return User.create(request)
                .map(repository::save)
                .map(User::getId);
    }

    @Override
    public Option<UserInfoDTO> getInfo(String id) {
        return Option.ofOptional(repository.getById(id))
                .map(UserAssembler::toDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Option<Either<Seq<ApiError>, UserInfoDTO>> edit(String id, UserEditDTO request) {

        // 因为对用户进行编辑需要先检索数据库，数据库属于基础设施层，所以不能在聚合根中直接编写，否则违背了DDD的设计理念
        // 所以为了达到实现的目的，我们这里就需要引入领域服务了UserDomainService
        // 因为先要通过用户名来查询用户信息是否存在是属于业务逻辑，应该放到领域层，不可暴露在外
        val result = userDomainService.edit(id, request);
        return result.map(t -> t.map(repository::edit).map(UserAssembler::toDTO));
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public Page<UserInfoDTO> list(Pagination page) {
        val result = repository.list(page);
        return UserAssembler.toPageDTO(result);
    }
}