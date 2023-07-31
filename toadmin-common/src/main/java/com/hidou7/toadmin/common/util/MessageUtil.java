/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.hidou7.toadmin.common.util;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 国际化异常提示处理
 */
public class MessageUtil {
    
    private static final MessageSource messageSource = SpringUtil.getBean(MessageSource.class);

    public static String getMessage(int code){
        return getMessage(code, new String[0]);
    }

    public static String getMessage(int code, String... params){
        return messageSource.getMessage(code + "", params, LocaleContextHolder.getLocale());
    }
}