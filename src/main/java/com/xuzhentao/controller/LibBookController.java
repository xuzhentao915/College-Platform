package com.xuzhentao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.common.Result;
import com.xuzhentao.model.dto.BookDTO;
import com.xuzhentao.model.vo.BookVO;
import com.xuzhentao.service.LibBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 图书管理控制器
 */
@Tag(name = "图书管理", description = "馆藏图书检索与管理")
@RestController
@RequestMapping("/library/books")
@RequiredArgsConstructor
public class LibBookController {

    private final LibBookService libBookService;

    @Operation(summary = "分页检索图书")
    @GetMapping
    public Result<Page<BookVO>> search(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(libBookService.searchBooks(page, size, keyword));
    }

    @Operation(summary = "获取图书详情")
    @GetMapping("/{id}")
    public Result<BookVO> detail(@PathVariable Long id) {
        return Result.success(libBookService.getBookDetail(id));
    }

    @Operation(summary = "新增馆藏图书")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody BookDTO dto) {
        libBookService.addBook(dto);
        return Result.success();
    }

    @Operation(summary = "更新馆藏图书")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody BookDTO dto) {
        libBookService.updateBook(id, dto);
        return Result.success();
    }
}
