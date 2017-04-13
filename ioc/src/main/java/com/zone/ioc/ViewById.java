package com.zone.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date: 2017/4/1.
 * @Author: Zone-Wonderful
 * @Description: View注解的Annotation
 */
//代表annotation的位置   FIELD 属性  TYPE 类上    CONSTRUTOR 构造函数  METHOD ctrl + shift + u 大小写切换
@Target({ElementType.FIELD})
//什么时候生效  CLASS 编译时      RUNTIME  运行时      SOURCE 源码资源
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    //--> @ViewById(R.id.tv)
    int value();
}
