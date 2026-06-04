package com.xuzhentao.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录成功响应
 */
@Data
@Builder
public class LoginVO {

    /** JWT 令牌 */
    private String token;

    /** 用户基本信息 */
    private UserVO userInfo;
}
