package com.feifei.ddd.demo.interfaces.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * 用户编辑请求dto
 *
 * @author xiaofeifei
 * @date 2020-02-03
 * @since
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEditDTO {

    /**
     * 用户名
     */
    String username;

    /**
     * 密码
     */
    String password;
}