package com.feifei.ddd.demo.interfaces.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * 查询用户信息详情dto
 *
 * @author shixiongfei
 * @date 2020-02-03
 * @since
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    String username;

    LocalDateTime createAt;

    LocalDateTime updateAt;
}