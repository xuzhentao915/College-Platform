package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 成绩视图对象
 */
@Data
@Builder
public class GradeVO {

    private Long id;
    private String term;
    private String courseCode;
    private String courseName;
    private BigDecimal credits;
    private String teacherName;
    private BigDecimal score;
    private BigDecimal gpaPoint;

    /** 是否挂科 (score < 60) */
    private Boolean isFailed;
}
