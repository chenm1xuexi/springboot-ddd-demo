package com.feifei.ddd.demo.infrastructure.jpa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feifei.ddd.demo.infrastructure.jpa.po.TbUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@Mapper
public interface UserMapper extends BaseMapper<TbUser> {
}
