package com.hidou7.toadmin.web.system.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.hidou7.toadmin.common.util.UUIDUtil;
import com.hidou7.toadmin.common.vo.R;
import com.hidou7.toadmin.web.system.RedisConstant;
import com.hidou7.toadmin.web.system.vo.CaptchaImageVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "验证码管理")
@RestController
@Slf4j
public class CaptchaController {
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @GetMapping("captchaImage")
    public R<CaptchaImageVo> captchaImage(){
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(160, 60);
        String code = captcha.getCode();
        String uuid = UUIDUtil.randomUUID();
        this.stringRedisTemplate.opsForValue().set(RedisConstant.captcha + uuid, code);
        return R.success(new CaptchaImageVo(uuid, captcha.getImageBase64()));
    }
}
