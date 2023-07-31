package com.hidou7.toadmin.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class PageDto {
    
    @NotNull
    private Integer pageNum;

    @NotNull
    @Max(value = 50)
    private Integer pageSize;
    
    public Page getPage(){
        return new Page(this.pageNum, this.pageSize);
    }
}
