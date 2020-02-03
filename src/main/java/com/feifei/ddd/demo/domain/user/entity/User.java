package com.feifei.ddd.demo.domain.user.entity;

import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.tool.IdWorker;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static io.vavr.API.*;
import static io.vavr.API.Right;

/**
 * 用户实体对象，尽量保证为充血模型
 *
 * 领域实体的业务逻辑尽量不要暴露在外界
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    String id;

    String username;

    String password;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    private User(UserCreate request) {
        val now = LocalDateTime.now();
        this.id = IdWorker.getId();
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.createAt = now;
        this.updateAt = now;
    }

    /**
     * 创建用户实体对象
     *
     * @author xiaofeifei
     * @date 2020-02-02
     * @updateDate 2020-02-02
     * @updatedBy xiaofeifei
     * @param
     * @return
     */
    public static Either<Seq<ApiError>, User> create(UserCreate request) {
        if (request.getPassword().length() < 6) {
            return Left(Seq(ApiError.create(1, "密码不足6位")));
        }

        return Right(new User(request));
    }
}