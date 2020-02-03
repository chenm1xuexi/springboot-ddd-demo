package com.feifei.ddd.demo.interfaces.validator;

import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import org.apache.commons.lang3.StringUtils;

import static io.vavr.API.Invalid;
import static io.vavr.API.Valid;

/**
 * 用户逻辑校验器
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
public class UserLogicValidator {


    /**
     * 对新增用户dto进行逻辑校验
     *
     * @param request 校验的请求参数对象
     * @return Either<Seq<ApiError>, UserCreate> 成功则返回UserCreate,失败则返回错误信息集合
     * @author xiaofeifei
     * @date 2020-02-02
     * @updateDate 2020-02-02
     * @updatedBy xiaofeifei
     */
    public static Either<Seq<ApiError>, UserCreate> validate(UserCreate request) {
        return validateUsername(request.getUsername())
                .combine(validatePassword(request.getPassword()))
                .ap((a, b) -> request)
                .toEither();
    }

    /**
     * 对编辑用户信息进行逻辑校验
     *
     * @author shixiongfei
     * @date 2020-02-03
     * @updateDate 2020-02-03
     * @updatedBy shixiongfei
     * @param
     * @return
     */
    public static Either<Seq<ApiError>, UserEditDTO> validate(UserEditDTO request) {
        return validateUsername(request.getUsername())
                .combine(validatePassword(request.getPassword()))
                .ap((a, b) -> request)
                .toEither();
    }

    private static Validation<ApiError, String> validatePassword(String password) {
        return StringUtils.isBlank(password)
                ? Invalid(ApiError.create(1, "密码为空"))
                : Valid(password);
    }

    private static Validation<ApiError, String> validateUsername(String username) {
        return StringUtils.isBlank(username)
                ? Invalid(ApiError.create(0, "用户名为空"))
                : Valid(username);
    }

}
