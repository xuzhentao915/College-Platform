package com.xuzhentao.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生成绩实体 — 对应 edu_grade 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("edu_grade")
public class EduGrade {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;
    private Long courseId;

    /** 学期 (如 2024-2025-1) */
    private String term;

    /** 考试得分 */
    private BigDecimal score;

    /** 换算绩点 */
    private BigDecimal gpaPoint;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
