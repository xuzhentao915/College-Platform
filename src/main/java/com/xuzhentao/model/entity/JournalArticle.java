package com.xuzhentao.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学术期刊文献实体 — 对应 journal_article 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("journal_article")
public class JournalArticle {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;
    private String authors;
    private String abstractText;
    private String sourceUrl;
    private LocalDate publishDate;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
