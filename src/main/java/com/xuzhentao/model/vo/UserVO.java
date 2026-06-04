package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象（脱敏后返回前端）
 */
@Data
@Builder
public class UserVO {

    private Long id;
    private String studentId;
    private String name;
    private String role;
    private String email;
    private String avatar;
    private LocalDateTime createTime;
}
