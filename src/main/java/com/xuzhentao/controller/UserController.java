package com.xuzhentao.controller;

import com.xuzhentao.common.Result;
import com.xuzhentao.model.vo.UserVO;
import com.xuzhentao.security.UserContext;
import com.xuzhentao.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器（需 Token 鉴权）
 */
@Tag(name = "用户中心", description = "个人信息管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = UserContext.getUserId();
        return Result.success(sysUserService.getUserInfo(userId));
    }

    @Operation(summary = "修改个人信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UserVO vo) {
        Long userId = UserContext.getUserId();
        sysUserService.updateUserInfo(userId, vo);
        return Result.success();
    }
}
