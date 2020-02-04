package com.feifei.ddd.demo.domain.user;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;

/**
 * 用户领域服务
 *
 * @author xiaofeifei
 * @date 2020-02-03
 * @since
 */
public interface UserDomainService {

    /**
     * 领域服务一般会将实体返回给应用服务，然后应用服务进行编排转发到接口层
     *
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     * @param
     * @return
     */
     Option<Either<Seq<ApiError>, User>> edit(String id, UserEditDTO request);
}