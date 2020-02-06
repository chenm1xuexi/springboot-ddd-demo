package com.feifei.ddd.demo.interfaces.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDateTime;

/**
 * 查询用户信息详情dto, 继承超文本驱动
 *
 * @author xiaofeifei
 * @date 2020-02-03
 * @since
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
@Relation(collectionRelation = "data")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO extends ResourceSupport {

    String id;

    String username;

    LocalDateTime createAt;

    LocalDateTime updateAt;
}