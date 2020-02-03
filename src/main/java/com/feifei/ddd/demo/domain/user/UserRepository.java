package com.feifei.ddd.demo.domain.user;

import com.feifei.ddd.demo.domain.user.entity.User;

import java.util.Optional;

/**
 * 用户仓储接口
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
public interface UserRepository {

    /**
     * 将用户实体持久化到仓储中，具体实现交由基础设施层的持久化工具来完成
     * 领域层并不关注到底如何持久化
     *
     * @author xiaofeifei
     * @date 2020-02-02
     * @updateDate 2020-02-02
     * @updatedBy xiaofeifei
     * @param user 聚合根
     * @return
     */
    User save(User user);

    /**
     * 通过主键来获取用户信息
     *
     * @author shixiongfei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy shixiongfei
     * @param id 主键id
     * @return
     */
    Optional<User> getById(String id);

    /**
     * 修改指定的用户信息
     *
     * @author shixiongfei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy shixiongfei
     * @param
     * @return
     */
    User edit(User user);
}
