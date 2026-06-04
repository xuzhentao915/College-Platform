package com.xuzhentao.controller;

import com.xuzhentao.common.Result;
import com.xuzhentao.model.dto.LoginDTO;
import com.xuzhentao.model.dto.RegisterDTO;
import com.xuzhentao.model.vo.LoginVO;
import com.xuzhentao.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器 — 登录/注册（无需 Token）
 */
@Tag(name = "认证管理", description = "用户登录与注册")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService sysUserService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(sysUserService.login(dto));
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        sysUserService.register(dto);
        return Result.success();
    }
}
