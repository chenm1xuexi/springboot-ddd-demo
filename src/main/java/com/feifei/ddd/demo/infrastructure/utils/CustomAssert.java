package com.feifei.ddd.demo.infrastructure.utils;

import com.feifei.ddd.demo.infrastructure.exception.FlyException;

/**
 * 自定义断言校验
 *
 * @author xiaofeifei
 * @date 2020-02-02
 * @since
 */
public final class CustomAssert {


    public static void greaterThanSource2Error(int source, int dest, String errorMsg) {
        if (dest > source) throw2Error(errorMsg);

    }

    public static void lessThanSource2Error(int source, int dest, String errorMsg) {
        if (dest < source) throw2Error(errorMsg);
    }

    public static void true2Error(boolean isTrue, String errorMsg) {
        if (isTrue) throw2Error(errorMsg);
    }

    public static void false2Error(boolean isFalse, String errorMsg) {
        true2Error(!isFalse, errorMsg);
    }

    public static void throw2Error(String errorMsg) {
        throw new FlyException(errorMsg);
    }
}