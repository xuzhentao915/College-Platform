package com.xuzhentao.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 文献新增请求体
 */
@Data
public class JournalDTO {

    @NotBlank(message = "文献标题不能为空")
    private String title;

    private String authors;
    private String abstractText;
    private String sourceUrl;
    private LocalDate publishDate;
}
