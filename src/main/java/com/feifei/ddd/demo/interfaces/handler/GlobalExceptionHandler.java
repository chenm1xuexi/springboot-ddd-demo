package com.feifei.ddd.demo.interfaces.handler;

import com.feifei.ddd.demo.infrastructure.ApiError;
import com.feifei.ddd.demo.infrastructure.exception.FlyException;
import com.feifei.ddd.demo.infrastructure.tool.Restful;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局服务端异常处理器
 *
 * 这里只是粗粒度的处理异常，可针对自己的业务类型的异常错误来进行细化
 *
 * @author shixiongfei
 * @date 2020-02-05
 * @since
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FlyException.class)
    public ResponseEntity error(FlyException e) {
        log.error(e.getMessage(), e);
        return Restful.error(ApiError.create(9999, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity error(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Restful.error(ApiError.create(10000, "服务端异常, 请联系管理员!"));
    }
}