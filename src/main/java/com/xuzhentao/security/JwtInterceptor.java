package com.xuzhentao.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuzhentao.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器 — 校验请求头中的 Bearer Token
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(AUTH_HEADER);
        boolean hasToken = StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX);
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 允许特定路径的 GET 请求匿名访问（无需 Token 即可访问）
        if ("GET".equalsIgnoreCase(method) && uri.startsWith("/api/market/goods")) {
            return true;
        }

        // 如果没有 Token，直接拦截（需要认证的接口必须提供 Token）
        if (!hasToken) {
            writeUnauthorized(response, "未提供有效的认证令牌");
            return false;
        }

        // 验证 Token 有效性
        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!jwtUtils.validateToken(token)) {
            // Token 无效，但如果是公开路径的 GET 请求，也放行（兼容过期 Token 的情况）
            if ("GET".equalsIgnoreCase(method)) {
                return true;
            }
            writeUnauthorized(response, "认证令牌已过期或无效");
            return false;
        }

        // 将用户信息写入 ThreadLocal
        Long userId = jwtUtils.getUserId(token);
        String role = jwtUtils.getRole(token);
        UserContext.set(UserContext.UserInfo.builder()
                .userId(userId)
                .role(role)
                .build());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(401, message)));
    }
}
