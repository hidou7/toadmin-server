package com.hidou7.toadmin.common.config;

import com.hidou7.toadmin.common.annotation.Auth;
import com.hidou7.toadmin.common.bo.CurBaseIdentityInfo;
import com.hidou7.toadmin.common.enums.ErrorCode;
import com.hidou7.toadmin.common.enums.Identity;
import com.hidou7.toadmin.common.exception.BusinessException;
import com.hidou7.toadmin.common.util.JsonUtil;
import com.hidou7.toadmin.common.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 鉴权拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TokenHelper tokenHelper;

    private static final ThreadLocal<String> authThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        Auth auth = method.getMethodAnnotation(Auth.class);
        if(auth == null){
            auth = method.getBeanType().getAnnotation(Auth.class);
            if(auth == null){
                return true;
            }
        }
        // 获取token信息
        String identityInfoStr = this.tokenHelper.decryptToStr(request.getHeader("token"));
        CurBaseIdentityInfo identityInfo = JsonUtil.parseObject(identityInfoStr, CurBaseIdentityInfo.class);
        if(identityInfo == null){
            throw new BusinessException(ErrorCode.loginExpired);
        }
        // 身份判断
        Identity identity = auth.identity();
        if(!identity.equals(identityInfo.getIdentity())){
            throw new BusinessException(ErrorCode.forbidden);
        }
        // 权限判断
        if(!identityInfo.checkPerm(auth.perm())){
            throw new BusinessException(ErrorCode.forbidden);
        }
        // token续期
        String redisKey = identityInfo.getRedisKey();
        Long expire = this.stringRedisTemplate.getExpire(redisKey);
        if(identity.checkRenewal(expire)){
            this.stringRedisTemplate.expire(redisKey, identity.timeout, TimeUnit.SECONDS);
        }
        authThreadLocal.set(identityInfoStr);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        authThreadLocal.remove();
    }
    
    public static String getCurIdentityInfo(){
        String identityInfo = authThreadLocal.get();
        if(identityInfo == null){
            throw new BusinessException(ErrorCode.loginExpired);
        }
        return identityInfo;
    }
}
