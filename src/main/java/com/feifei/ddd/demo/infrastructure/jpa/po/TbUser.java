package com.feifei.ddd.demo.infrastructure.jpa.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@TableName("tb_user")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TbUser {

    @TableId(value = ID, type = IdType.INPUT)
    String id;

    @TableField(USERNAME)
    String username;

    @TableField(PASSWORD)
    String password;

    @TableField(CREATE_AT)
    LocalDateTime createAt;

    @TableField(UPDATE_AT)
    LocalDateTime updateAt;

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CREATE_AT = "create_at";
    public static final String UPDATE_AT = "update_at";
}