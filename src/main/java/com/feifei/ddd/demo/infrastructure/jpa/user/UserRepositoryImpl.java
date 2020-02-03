package com.feifei.ddd.demo.infrastructure.jpa.user;

import com.feifei.ddd.demo.domain.user.UserRepository;
import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.jpa.mapper.UserMapper;
import com.feifei.ddd.demo.infrastructure.jpa.po.TbUser;
import com.feifei.ddd.demo.infrastructure.utils.CustomAssert;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import static com.feifei.ddd.demo.infrastructure.constant.error.UserErrorConstants.ADD_USER_FAILED;

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
        TbUser tbUser = new TbUser();
        BeanUtils.copyProperties(user, tbUser);
        CustomAssert.lessThanSource2Error(1, userMapper.insert(tbUser),ADD_USER_FAILED);
        return user;
    }
}
