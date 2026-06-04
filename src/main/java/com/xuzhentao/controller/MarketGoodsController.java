package com.xuzhentao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.common.Result;
import com.xuzhentao.model.dto.GoodsDTO;
import com.xuzhentao.model.vo.GoodsVO;
import com.xuzhentao.security.UserContext;
import com.xuzhentao.service.MarketGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 二手闲置交易控制器
 */
@Tag(name = "二手交易", description = "闲置商品发布与管理")
@RestController
@RequestMapping("/market/goods")
@RequiredArgsConstructor
public class MarketGoodsController {

    private final MarketGoodsService marketGoodsService;

    @Operation(summary = "发布商品")
    @PostMapping
    public Result<Void> publish(@Valid @RequestBody GoodsDTO dto) {
        marketGoodsService.publish(UserContext.getUserId(), dto);
        return Result.success();
    }

    @Operation(summary = "分页查询商品列表")
    @GetMapping
    public Result<Page<GoodsVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        return Result.success(marketGoodsService.listGoods(page, size, keyword, category));
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public Result<GoodsVO> detail(@PathVariable Long id) {
        return Result.success(marketGoodsService.getGoodsDetail(id));
    }

    @Operation(summary = "修改商品信息")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody GoodsDTO dto) {
        marketGoodsService.updateGoods(UserContext.getUserId(), id, dto);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        marketGoodsService.deleteGoods(UserContext.getUserId(), id);
        return Result.success();
    }

    @Operation(summary = "修改商品状态")
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam String status) {
        marketGoodsService.changeStatus(UserContext.getUserId(), id, status);
        return Result.success();
    }
}
