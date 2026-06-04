package com.xuzhentao.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求体
 */
@Data
public class LoginDTO {

    @NotBlank(message = "学号/工号不能为空")
    private String studentId;

    @NotBlank(message = "密码不能为空")
    private String password;
}
