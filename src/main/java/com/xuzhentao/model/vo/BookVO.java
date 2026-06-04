package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 图书视图对象
 */
@Data
@Builder
public class BookVO {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer totalStock;
    private Integer currentStock;
    private String location;
}
