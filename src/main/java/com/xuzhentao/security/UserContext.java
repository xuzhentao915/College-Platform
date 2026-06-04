package com.xuzhentao.security;

import lombok.Builder;
import lombok.Data;

/**
 * 当前登录用户上下文 — 基于 ThreadLocal 持有当前请求的用户身份
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> CURRENT_USER = new ThreadLocal<>();

    public static void set(UserInfo userInfo) {
        CURRENT_USER.set(userInfo);
    }

    public static UserInfo get() {
        return CURRENT_USER.get();
    }

    public static Long getUserId() {
        UserInfo info = CURRENT_USER.get();
        return info != null ? info.getUserId() : null;
    }

    public static String getRole() {
        UserInfo info = CURRENT_USER.get();
        return info != null ? info.getRole() : null;
    }

    public static void clear() {
        CURRENT_USER.remove();
    }

    @Data
    @Builder
    public static class UserInfo {
        private Long userId;
        private String role;
    }
}
