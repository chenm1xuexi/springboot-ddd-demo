package com.feifei.ddd.demo.infrastructure.jpa.assembler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.jpa.po.TbUser;
import com.feifei.ddd.demo.infrastructure.tool.Pagination;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 持久层实体对象转PO
 *
 * @author xiaofeifei
 * @date 2020-02-03
 * @since
 */
public interface UserAssembler {

    static TbUser toPO(User user) {
        TbUser tbUser = new TbUser();
        BeanUtils.copyProperties(user, tbUser);
        return tbUser;
    }

    static User toEntity(TbUser tbUser) {
        if (Objects.isNull(tbUser)) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(tbUser, user);
        return user;
    }

    /**
     * 这里为什么要采用如此复杂的方式来做转换呢，因为是为了考虑到
     * 领域层和基础设施层的实现是解耦的，领域层并不关注数据是如何存储的以及
     * 如何从持久层获取到数据，既然解耦，那么就不应该依赖于基础设施层中的mybatis-plus分页类
     *
     * @author shixiongfei
     * @date 2020-02-06
     * @updateDate 2020-02-06
     * @updatedBy shixiongfei
     * @param
     * @return
     */
    static Pagination<User> toPageEntity(Page<TbUser> page, Pagination<User> pagination) {
        var users = page.getRecords().stream().map(tbUser -> {
            User user = new User();
            user.setId(tbUser.getId());
            user.setUsername(tbUser.getUsername());
            user.setPassword(tbUser.getPassword());
            user.setCreateAt(tbUser.getCreateAt());
            user.setUpdateAt(tbUser.getUpdateAt());
            return user;
        }).collect(Collectors.toList());
        pagination.setPages(page.getPages());
        pagination.setTotal(page.getTotal());
        pagination.setData(users);
        return pagination;
    }
}