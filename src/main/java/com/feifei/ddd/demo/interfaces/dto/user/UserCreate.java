package com.feifei.ddd.demo.interfaces.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class UserCreate {

    String username;

    String password;

}
