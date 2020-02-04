package com.feifei.ddd.demo.infrastructure.jpa.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.jpa.assembler.UserAssembler;
import com.feifei.ddd.demo.infrastructure.jpa.mapper.UserMapper;
import com.feifei.ddd.demo.infrastructure.jpa.po.TbUser;
import com.feifei.ddd.demo.infrastructure.utils.CustomAssert;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.feifei.ddd.demo.infrastructure.constant.error.UserErrorConstants.ADD_USER_FAILED;
import static com.feifei.ddd.demo.infrastructure.constant.error.UserErrorConstants.UPDATE_USER_FAILED;

/**
 * 用户仓储实现类，用于对领域对象进行持久化
 * 其实这里应该有一个转换层，就是将领域对象转换为1个或多个PO对象，然后对po对象进行持久化
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@Repository
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepositoryImpl implements UserRepository {

    UserMapper userMapper;

    @Override
    public User save(User user) {
        // 这里需要一层转换，我们可以抽象出一个编译类，专门用于对象间的数据交换UserAssembler
        val tbUser = UserAssembler.toPO(user);
        CustomAssert.lessThanSource2Error(1, userMapper.insert(tbUser), ADD_USER_FAILED);
        return user;
    }

    @Override
    public Optional<User> getById(String id) {
        return Optional.ofNullable(UserAssembler.toEntity(userMapper.selectById(id)));
    }

    @Override
    public User edit(User user) {
        var tbUser = UserAssembler.toPO(user);
        CustomAssert.lessThanSource2Error(1, userMapper.updateById(tbUser), UPDATE_USER_FAILED);
        return user;
    }

    @Override
    public Option<User> getByUsername(String username) {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        val tbUser = userMapper.selectOne(wrapper);
        return Option.of(tbUser).map(UserAssembler::toEntity);
    }

    @Override
    public void delete(String id) {
        userMapper.deleteById(id);
    }
}