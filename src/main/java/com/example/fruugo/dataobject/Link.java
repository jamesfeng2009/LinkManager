package com.example.fruugo.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Link {
    /**
     * 跳转链接id
     */
    private Long id;

    /**
     * 跳转链接地址
     */
    @NotEmpty(message = "跳转链接地址不能为空！")
    private String link;

    /**
     * 链接是否被删除
     */
    private Integer status;
}
