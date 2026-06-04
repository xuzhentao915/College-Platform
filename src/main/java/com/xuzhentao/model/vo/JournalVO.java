package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 文献视图对象
 */
@Data
@Builder
public class JournalVO {

    private Long id;
    private String title;
    private String authors;
    private String abstractText;
    private String sourceUrl;
    private LocalDate publishDate;
    private LocalDateTime createTime;
}
