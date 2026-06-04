package com.xuzhentao.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局统一响应体结构封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /** 状态码 (200=成功，500=失败，401=未授权) */
    private Integer code;

    /** 对用户的友好提示信息 */
    private String message;

    /** 返回的具体业务荷载数据 */
    private T data;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder().code(200).message("操作成功").data(data).build();
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return Result.<T>builder().code(code).message(message).build();
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}
