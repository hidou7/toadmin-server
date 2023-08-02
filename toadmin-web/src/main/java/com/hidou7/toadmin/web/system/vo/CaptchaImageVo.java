package com.hidou7.toadmin.web.system.vo;

import lombok.Data;

@Data
public class CaptchaImageVo {
    
    private String uuid;
    
    private String img;

    public CaptchaImageVo() {
    }

    public CaptchaImageVo(String uuid, String img) {
        this.uuid = uuid;
        this.img = img;
    }
}
