package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品视图对象
 */
@Data
@Builder
public class GoodsVO {

    private Long id;
    private Long sellerId;
    private String sellerName;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String images;
    private String status;
    private LocalDateTime createTime;
}
