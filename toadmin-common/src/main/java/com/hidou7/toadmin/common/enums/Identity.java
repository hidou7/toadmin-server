package com.hidou7.toadmin.common.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Identity {

    user(60*60*24, 0.3);

    /**
     * token有效时间（秒）
     */
    public final Integer timeout;

    /**
     * token续期因子
     *  剩余时间 / timeout >= renFactor 就会触发token续期
     *  <=0：直接续期
     *  >=1：不续期
     */
    public final Double renFactor;

    Identity(Integer timeout, Double renFactor) {
        this.timeout = timeout;
        this.renFactor = renFactor;
    }

    /**
     * token续期判断
     */
    public boolean checkRenewal(Long expire){
        if(expire != null && expire > 0){
            if(this.renFactor >= 1) return false;
            if(this.renFactor <= 0) return true;
            double factor = BigDecimal.valueOf(expire)
                    .divide(BigDecimal.valueOf(this.timeout), 1, RoundingMode.HALF_UP)
                    .doubleValue();
            return factor >= this.renFactor;
        }
        return false;
    }
}
