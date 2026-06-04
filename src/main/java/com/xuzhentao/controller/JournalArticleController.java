package com.xuzhentao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.common.Result;
import com.xuzhentao.model.dto.JournalDTO;
import com.xuzhentao.model.vo.JournalVO;
import com.xuzhentao.service.JournalArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 学术期刊控制器
 */
@Tag(name = "学术期刊", description = "文献浏览与管理")
@RestController
@RequestMapping("/journal/articles")
@RequiredArgsConstructor
public class JournalArticleController {

    private final JournalArticleService journalArticleService;

    @Operation(summary = "分页浏览文献列表")
    @GetMapping
    public Result<Page<JournalVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(journalArticleService.listArticles(page, size, keyword));
    }

    @Operation(summary = "获取文献详情")
    @GetMapping("/{id}")
    public Result<JournalVO> detail(@PathVariable Long id) {
        return Result.success(journalArticleService.getArticleDetail(id));
    }

    @Operation(summary = "新增文献")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody JournalDTO dto) {
        journalArticleService.addArticle(dto);
        return Result.success();
    }
}
