package com.hidou7.toadmin.common.annotation;


import com.hidou7.toadmin.common.enums.Identity;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

    Identity identity();

    String[] perm() default {};
}
