package com.feifei.ddd.demo.interfaces.assembler;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * 用户领域实体和dto互转编译器
 * @author xiaofeifei
 * @date 2020-02-03
 * @since
 */
public interface UserAssembler {

    static UserInfoDTO toDTO(User entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        UserInfoDTO infoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(entity, infoDTO);
        return infoDTO;
    }
}
