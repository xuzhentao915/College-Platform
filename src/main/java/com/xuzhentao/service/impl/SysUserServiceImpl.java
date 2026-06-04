package com.xuzhentao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuzhentao.exception.BusinessException;
import com.xuzhentao.mapper.SysUserMapper;
import com.xuzhentao.model.dto.LoginDTO;
import com.xuzhentao.model.dto.RegisterDTO;
import com.xuzhentao.model.entity.SysUser;
import com.xuzhentao.model.vo.LoginVO;
import com.xuzhentao.model.vo.UserVO;
import com.xuzhentao.security.JwtUtils;
import com.xuzhentao.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO dto) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getStudentId, dto.getStudentId()));

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "学号或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(403, "该账号已被禁用，请联系管理员");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getRole());
        return LoginVO.builder()
                .token(token)
                .userInfo(toUserVO(user))
                .build();
    }

    @Override
    public void register(RegisterDTO dto) {
        // 检查学号是否已存在
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getStudentId, dto.getStudentId()));
        if (count > 0) {
            throw new BusinessException("该学号已被注册");
        }

        SysUser user = SysUser.builder()
                .studentId(dto.getStudentId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .email(dto.getEmail())
                .role("STUDENT")
                .status(1)
                .build();
        sysUserMapper.insert(user);
        log.info("新用户注册成功: studentId={}", dto.getStudentId());
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        SysUser user = Optional.ofNullable(sysUserMapper.selectById(userId))
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return toUserVO(user);
    }

    @Override
    public void updateUserInfo(Long userId, UserVO vo) {
        SysUser user = Optional.ofNullable(sysUserMapper.selectById(userId))
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setName(vo.getName());
        user.setEmail(vo.getEmail());
        user.setAvatar(vo.getAvatar());
        sysUserMapper.updateById(user);
    }

    private UserVO toUserVO(SysUser user) {
        return UserVO.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .name(user.getName())
                .role(user.getRole())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .createTime(user.getCreateTime())
                .build();
    }
}
