package com.feifei.ddd.demo.infrastructure.exception;

/**
 * 自定义异常类
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
public class FlyException extends RuntimeException {

    public FlyException(String errorMsg) {
        super(errorMsg);
    }
}