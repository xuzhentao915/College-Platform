package com.xuzhentao.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 二手交易商品实体 — 对应 market_goods 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_goods")
public class MarketGoods {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 发布者用户ID */
    private Long sellerId;

    /** 商品标题 */
    private String title;

    /** 商品描述 */
    private String description;

    /** 交易价格 */
    private BigDecimal price;

    /** 商品分类 */
    private String category;

    /** 图片关联 JSON */
    private String images;

    /** 状态: ON_SALE, SOLD, OFF_SHELF */
    private String status;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
