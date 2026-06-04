package com.xuzhentao.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 图书馆藏实体 — 对应 lib_book 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("lib_book")
public class LibBook {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer totalStock;
    private Integer currentStock;
    private String location;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
