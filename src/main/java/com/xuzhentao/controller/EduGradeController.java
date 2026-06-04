package com.xuzhentao.controller;

import com.xuzhentao.common.Result;
import com.xuzhentao.model.vo.GpaSummaryVO;
import com.xuzhentao.model.vo.GradeVO;
import com.xuzhentao.security.UserContext;
import com.xuzhentao.service.EduGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 教务成绩控制器
 */
@Tag(name = "成绩管理", description = "学生成绩查询与GPA分析")
@RestController
@RequestMapping("/academic")
@RequiredArgsConstructor
public class EduGradeController {

    private final EduGradeService eduGradeService;

    @Operation(summary = "查询成绩列表（按学期分组）")
    @GetMapping("/grades")
    public Result<Map<String, List<GradeVO>>> getGrades() {
        Long userId = UserContext.getUserId();
        return Result.success(eduGradeService.getGradesByTerm(userId));
    }

    @Operation(summary = "获取 GPA 汇总")
    @GetMapping("/gpa")
    public Result<GpaSummaryVO> getGpaSummary() {
        Long userId = UserContext.getUserId();
        return Result.success(eduGradeService.getGpaSummary(userId));
    }
}
