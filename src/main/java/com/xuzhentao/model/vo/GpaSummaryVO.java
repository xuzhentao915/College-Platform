package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * GPA 汇总视图
 */
@Data
@Builder
public class GpaSummaryVO {

    /** 总学分 */
    private BigDecimal totalCredits;

    /** 已获学分 */
    private BigDecimal earnedCredits;

    /** 加权平均绩点 */
    private BigDecimal weightedGpa;

    /** 总课程数 */
    private Integer totalCourses;

    /** 挂科数 */
    private Integer failedCourses;
}
