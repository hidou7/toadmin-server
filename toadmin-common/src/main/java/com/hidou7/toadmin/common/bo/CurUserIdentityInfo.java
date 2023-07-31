package com.hidou7.toadmin.common.bo;

import com.hidou7.toadmin.common.config.AuthInterceptor;
import com.hidou7.toadmin.common.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurUserIdentityInfo extends CurBaseIdentityInfo{

    private String phone;

    public static CurUserIdentityInfo getInfo() {
        String identityInfo = AuthInterceptor.getCurIdentityInfo();
        return JsonUtil.parseObject(identityInfo, CurUserIdentityInfo.class);
    }
    
    public static Long getUserId(){
        return getInfo().getId();
    }
}
