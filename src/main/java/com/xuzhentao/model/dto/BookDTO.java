package com.xuzhentao.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 图书新增/编辑请求体
 */
@Data
public class BookDTO {

    @NotBlank(message = "ISBN 不能为空")
    private String isbn;

    @NotBlank(message = "书名不能为空")
    private String title;

    @NotBlank(message = "作者不能为空")
    private String author;

    private String publisher;

    @NotNull(message = "总库存不能为空")
    @PositiveOrZero(message = "库存不能为负数")
    private Integer totalStock;

    @NotNull(message = "当前库存不能为空")
    @PositiveOrZero(message = "库存不能为负数")
    private Integer currentStock;

    private String location;
}
