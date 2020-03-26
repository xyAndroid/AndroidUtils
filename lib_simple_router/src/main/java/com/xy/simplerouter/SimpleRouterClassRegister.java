package com.xy.simplerouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由注解
 *
 * @author kingpang
 * @date 2018/9/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SimpleRouterClassRegister {
    String key();

    @SimpleRouterObj.SimpleRouterRegisterType
    int type();
}
