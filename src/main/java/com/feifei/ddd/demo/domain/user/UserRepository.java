package com.feifei.ddd.demo.domain.user;

import com.feifei.ddd.demo.domain.user.entity.User;

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
     * @param
     * @return
     */
    User save(User user);
}
