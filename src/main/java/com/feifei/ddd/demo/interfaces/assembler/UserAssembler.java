package com.feifei.ddd.demo.interfaces.assembler;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.infrastructure.tool.Pagination;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户领域实体和dto互转编译器
 *
 * @author xiaofeifei
 * @date 2020-02-03
 * @since
 */
public interface UserAssembler {

    static Page<UserInfoDTO> toPageDTO(Pagination<User> page) {
        System.out.println("11");
        val pageRequest = PageRequest.of((int) page.getCurrent(), (int) page.getSize());
        if (page.getTotal() == 0) {
            return new PageImpl<>(Collections.emptyList(), pageRequest , page.getTotal());
        }

        val list = page.getData().stream()
                .map(user -> new UserInfoDTO(user.getId(), user.getUsername(), user.getCreateAt(), user.getUpdateAt()))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageRequest, page.getTotal());
    }

    static UserInfoDTO toDTO(User entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return new UserInfoDTO(entity.getId(), entity.getUsername(), entity.getCreateAt(), entity.getUpdateAt());
    }
}
