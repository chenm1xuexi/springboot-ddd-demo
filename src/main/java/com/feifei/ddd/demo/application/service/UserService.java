package com.feifei.ddd.demo.application.service;

import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;

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

    /**
     * 通过资源主键来查询用户详情信息
     *
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     * @param id 资源主键id
     * @return
     */
    Option<UserInfoDTO> getInfo(String id);

    /**
     * 编辑这里采用3级rest来表达，id是拼接在uri之后的参数
     * 更新成功后，返回UserInfoDTO给客户
     * user => userInfoDTO在应用服务层进行编排转换
     *
     * @author xiaofeifei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy xiaofeifei
     * @param
     * @return
     */
    Option<Either<Seq<ApiError>, UserInfoDTO>> edit(String id, UserEditDTO request);

    /**
     * 通过资源主键来移除用户信息
     *
     * @author xiaofeifei
     * @date 2020-02-04
     * @updateDate 2020-02-04
     * @updatedBy xiaofeifei
     * @param
     * @return
     */
    void delete(String id);
}
