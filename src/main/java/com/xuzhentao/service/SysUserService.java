package com.xuzhentao.service;

import com.xuzhentao.model.dto.LoginDTO;
import com.xuzhentao.model.dto.RegisterDTO;
import com.xuzhentao.model.vo.LoginVO;
import com.xuzhentao.model.vo.UserVO;

/**
 * 用户服务接口
 */
public interface SysUserService {

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 用户注册
     */
    void register(RegisterDTO dto);

    /**
     * 获取当前登录用户信息
     */
    UserVO getUserInfo(Long userId);

    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, UserVO vo);
}
