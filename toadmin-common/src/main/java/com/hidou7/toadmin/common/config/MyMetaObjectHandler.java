package com.hidou7.toadmin.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hidou7.toadmin.common.bo.CurBaseIdentityInfo;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = this.getUserId();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "creator", Long.class, userId);
        this.strictInsertFill(metaObject, "updater", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = this.getUserId();
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        this.strictUpdateFill(metaObject, "updater", Long.class, userId);
    }
    
    private Long getUserId(){
        Long userId = null;
        try{
            userId = CurBaseIdentityInfo.getInfo().getId();
        }catch (Exception ignored){
        }
        return userId;
    }
}
