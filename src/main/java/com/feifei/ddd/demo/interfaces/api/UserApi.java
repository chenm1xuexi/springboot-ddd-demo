package com.feifei.ddd.demo.interfaces.api;

import com.feifei.ddd.demo.application.service.UserService;
import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.constant.ApiConstant;
import com.feifei.ddd.demo.infrastructure.tool.Restful;
import com.feifei.ddd.demo.interfaces.dto.user.UserEditDTO;
import com.feifei.ddd.demo.interfaces.dto.user.UserInfoDTO;
import com.feifei.ddd.demo.interfaces.validator.UserLogicValidator;
import com.feifei.ddd.demo.interfaces.dto.user.UserCreate;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;

/**
 * 用户接口层
 * 用于对外提供用户接口服务
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
@RestController
@AllArgsConstructor
@RequestMapping(ApiConstant.USER_ENDPOINT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserApi {

    /**
     * 这里采用构造器注入的方式，不采用@Autowired属性注入
     */
    UserService service;

    @PostMapping
    public ResponseEntity create(@RequestBody UserCreate request) {

        // 用户逻辑校验器对参数进行校验（一般在校验器中的校验都是通用校验，意味着这种校验会发生在多处）
        val result = UserLogicValidator.validate(request).map(service::create);

        // left => 400
        // right => 201
        // 这里是对结果进行了一次模式匹配，
        // 如果为left则代表校验失败，返回错误信息
        // 为right则代表校验成功，返回响应信息(用户唯一标识id)
        return API.Match(result).of(
                Case($Left($()), Restful::badRequest),
                Case($Right($()), Restful::created));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable String id) {
        return Restful.ok(service.getInfo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable String id, @RequestBody UserEditDTO request) {
        // 校验成功则调用编辑操作，失败则返回错误信息
        // 1.Left 400
        // 2.Right None 404
        // 3.Right Some Left 400
        // 4.Right Some Right 200
         val result = UserLogicValidator.validate(request).map(req -> service.edit(id, req));

        return Match(result).of(
                Case($Left($()), Restful::badRequest),
                Case($Right($None()), Restful::notFound),
                Case($Right($Some($Left($()))), error -> Restful.badRequest(error.get().getLeft())),
                Case($Right($Some($Right($()))), msg -> Restful.ok(msg.get().get())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        service.delete(id);
        return Restful.noContent();
    }

}