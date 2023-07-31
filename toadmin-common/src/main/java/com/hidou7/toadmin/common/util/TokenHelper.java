package com.hidou7.toadmin.common.util;

import com.hidou7.toadmin.common.bo.CurBaseIdentityInfo;
import com.hidou7.toadmin.common.enums.Identity;
import com.hidou7.toadmin.common.exception.BusinessException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class TokenHelper {

    /**
     * UUID生成
     */
    @Value("${token.secretKey}")
    private String secretKey;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    private static final String encryptMode = "AES/ECB/PKCS5Padding";
    
    private String redisPrefix(Identity identity, Long userId){
        return "login:" + identity + ":" + userId + ":";
    }

    /**
     * 退出登录
     */
    public void logout(CurBaseIdentityInfo identityInfo){
        Set<String> keys = this.stringRedisTemplate.keys(
                this.redisPrefix(identityInfo.getIdentity(), identityInfo.getId()) + "*");
        if(keys != null && keys.size() > 0){
            this.stringRedisTemplate.delete(keys);
        }
    }

    /**
     * 生成token
     */
    @SneakyThrows
    public String createToken(CurBaseIdentityInfo identityInfo){
        Identity identity = identityInfo.getIdentity();
        Long userId = identityInfo.getId();
        String uuid = UUIDUtil.randomUUID().substring(16);
        String redisKey = this.redisPrefix(identity, userId) + uuid;
        String token = this.encryptStr(redisKey);
        identityInfo.setToken(token);
        identityInfo.setRedisKey(redisKey);
        this.logout(identityInfo);
        this.stringRedisTemplate.opsForValue().set(redisKey, JsonUtil.toJsonString(identityInfo), 
                identity.timeout, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 解密
     */
    @SneakyThrows
    public String decryptToStr(String token) {
        if(token == null || token.length() == 0) return null;
        Cipher cipher = this.initCipher(Cipher.DECRYPT_MODE);
        String redisKey = null;
        try{
            redisKey = new String(cipher.doFinal(base64ToBytes(token)), StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new BusinessException("token解码失败");
        }
        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    @SneakyThrows
    public CurBaseIdentityInfo decryptToInfo(String token){
        return JsonUtil.parseObject(this.decryptToStr(token), CurBaseIdentityInfo.class);
    }

    /**
     * 加密
     */
    private String encryptStr(String data) throws Exception {
        Cipher cipher = this.initCipher(Cipher.ENCRYPT_MODE);
        return bytesToBase64(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private Cipher initCipher(int opmode) throws Exception{
        Cipher cipher = Cipher.getInstance(encryptMode);
        SecretKey keySpec = new SecretKeySpec(base64ToBytes(this.secretKey), "AES");
        cipher.init(opmode, keySpec);
        return cipher;
    }

    private static byte[] base64ToBytes(String base64){
        return Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToBase64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
}
