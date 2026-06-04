package com.xuzhentao.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品发布/编辑请求体
 */
@Data
public class GoodsDTO {

    @NotBlank(message = "商品标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须大于0")
    private BigDecimal price;

    private String category;

    /** 图片 URL 列表（前端序列化为 JSON 字符串） */
    private String images;
}
