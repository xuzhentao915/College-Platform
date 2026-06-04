package com.xuzhentao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuzhentao.mapper.EduCourseMapper;
import com.xuzhentao.mapper.EduGradeMapper;
import com.xuzhentao.model.entity.EduCourse;
import com.xuzhentao.model.entity.EduGrade;
import com.xuzhentao.model.vo.GpaSummaryVO;
import com.xuzhentao.model.vo.GradeVO;
import com.xuzhentao.service.EduGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 成绩管理服务实现
 */
@Service
@RequiredArgsConstructor
public class EduGradeServiceImpl implements EduGradeService {

    private final EduGradeMapper eduGradeMapper;
    private final EduCourseMapper eduCourseMapper;

    @Override
    public Map<String, List<GradeVO>> getGradesByTerm(Long userId) {
        List<EduGrade> grades = eduGradeMapper.selectList(
                new LambdaQueryWrapper<EduGrade>().eq(EduGrade::getUserId, userId)
                        .orderByDesc(EduGrade::getTerm));

        return grades.stream()
                .map(this::toGradeVO)
                .collect(Collectors.groupingBy(GradeVO::getTerm));
    }

    @Override
    public GpaSummaryVO getGpaSummary(Long userId) {
        List<EduGrade> grades = eduGradeMapper.selectList(
                new LambdaQueryWrapper<EduGrade>().eq(EduGrade::getUserId, userId));

        BigDecimal totalCredits = BigDecimal.ZERO;
        BigDecimal earnedCredits = BigDecimal.ZERO;
        BigDecimal weightedSum = BigDecimal.ZERO;
        int failedCount = 0;

        for (EduGrade grade : grades) {
            EduCourse course = eduCourseMapper.selectById(grade.getCourseId());
            if (course == null) {
                continue;
            }
            BigDecimal credits = course.getCredits();
            totalCredits = totalCredits.add(credits);

            BigDecimal score = Optional.ofNullable(grade.getScore()).orElse(BigDecimal.ZERO);
            if (score.compareTo(new BigDecimal("60")) < 0) {
                failedCount++;
            } else {
                earnedCredits = earnedCredits.add(credits);
            }

            BigDecimal gpa = Optional.ofNullable(grade.getGpaPoint()).orElse(BigDecimal.ZERO);
            weightedSum = weightedSum.add(credits.multiply(gpa));
        }

        BigDecimal weightedGpa = totalCredits.compareTo(BigDecimal.ZERO) > 0
                ? weightedSum.divide(totalCredits, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return GpaSummaryVO.builder()
                .totalCredits(totalCredits)
                .earnedCredits(earnedCredits)
                .weightedGpa(weightedGpa)
                .totalCourses(grades.size())
                .failedCourses(failedCount)
                .build();
    }

    private GradeVO toGradeVO(EduGrade grade) {
        EduCourse course = eduCourseMapper.selectById(grade.getCourseId());
        BigDecimal score = Optional.ofNullable(grade.getScore()).orElse(BigDecimal.ZERO);
        return GradeVO.builder()
                .id(grade.getId())
                .term(grade.getTerm())
                .courseCode(course != null ? course.getCourseCode() : "")
                .courseName(course != null ? course.getCourseName() : "未知课程")
                .credits(course != null ? course.getCredits() : BigDecimal.ZERO)
                .teacherName(course != null ? course.getTeacherName() : "")
                .score(grade.getScore())
                .gpaPoint(grade.getGpaPoint())
                .isFailed(score.compareTo(new BigDecimal("60")) < 0)
                .build();
    }
}
