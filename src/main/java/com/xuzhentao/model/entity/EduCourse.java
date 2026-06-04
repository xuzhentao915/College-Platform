package com.xuzhentao.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 教务课程实体 — 对应 edu_course 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("edu_course")
public class EduCourse {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String courseCode;
    private String courseName;
    private BigDecimal credits;
    private String teacherName;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
