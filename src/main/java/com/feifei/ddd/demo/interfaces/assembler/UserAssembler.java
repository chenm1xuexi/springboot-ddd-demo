package com.feifei.ddd.demo.interfaces.assembler;

import com.feifei.ddd.demo.domain.user.entity.User;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import org.springframework.beans.BeanUtils;

/**
 * 用户领域实体和dto互转编译器
 * @author shixiongfei
 * @date 2020-02-03
 * @since
 */
public interface UserAssembler {

    static UserInfoDTO toDTO(User entity) {
        UserInfoDTO infoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(entity, infoDTO);
        return infoDTO;
    }
}
