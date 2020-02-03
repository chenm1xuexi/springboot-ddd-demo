package com.feifei.ddd.demo.application.service;

import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

/**
 * 用于应用服务层，用于对接口请求
 * 进行编排和转发
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
public interface UserService {

    /**
     * 用户创建
     *
     * @author xiaofeifei
     * @date 2020-02-02
     * @updateDate 2020-02-02
     * @updatedBy xiaofeifei
     * @param userCreate 用户创建dto实例
     * @return
     */
    Either<Seq<ApiError>, String> create(UserCreate userCreate);
}
