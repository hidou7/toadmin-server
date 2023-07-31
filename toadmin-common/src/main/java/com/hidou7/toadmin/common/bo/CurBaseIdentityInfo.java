package com.hidou7.toadmin.common.bo;

import com.hidou7.toadmin.common.config.AuthInterceptor;
import com.hidou7.toadmin.common.enums.Identity;
import com.hidou7.toadmin.common.util.JsonUtil;
import lombok.Data;

import java.util.List;

@Data
public class CurBaseIdentityInfo {
    
    private Long id;

    private String name;
    
    private String loginNo;

    private Identity identity;

    private Boolean isAdmin;

    private List<String> permissions;

    private String token;
    
    private String redisKey;

    public static CurBaseIdentityInfo getInfo() {
        String identityInfo = AuthInterceptor.getCurIdentityInfo();
        return JsonUtil.parseObject(identityInfo, CurBaseIdentityInfo.class);
    }
    
    public boolean checkPerm(String[] perms){
        if(perms == null || perms.length == 0 || Boolean.TRUE.equals(isAdmin)) return true;
        if(permissions == null || permissions.size() == 0) return false;
        for (String perm : perms) {
            if(permissions.contains(perm)){
                return true;
            }
        }
        return false;
    }
}
