package com.xuzhentao.service;

import com.xuzhentao.model.vo.GpaSummaryVO;
import com.xuzhentao.model.vo.GradeVO;

import java.util.List;
import java.util.Map;

/**
 * 成绩管理服务接口
 */
public interface EduGradeService {

    /** 查询学生成绩列表（按学期分组） */
    Map<String, List<GradeVO>> getGradesByTerm(Long userId);

    /** 计算学生 GPA 汇总 */
    GpaSummaryVO getGpaSummary(Long userId);
}
