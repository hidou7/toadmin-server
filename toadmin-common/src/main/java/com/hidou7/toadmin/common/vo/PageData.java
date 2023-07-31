package com.hidou7.toadmin.common.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hidou7.toadmin.common.util.JsonUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PageData<T> {
    
    private Long total;
    
    private List<T> list;
    
    public static <T> PageData<T> of(IPage<T> page){
        return new PageData<>(page.getRecords(), page.getTotal());
    }

    private PageData(List<T> list, Long total) {
        this.total = total;
        this.list = list;
    }

    public static void main(String[] args) {
        System.out.println(JsonUtil.toJsonString(LocalDateTime.now()));
    }
}
