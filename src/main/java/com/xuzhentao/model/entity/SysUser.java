package com.xuzhentao.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统用户实体 — 对应 sys_user 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 学号/工号 */
    private String studentId;

    /** 密码 (BCrypt) */
    private String password;

    /** 姓名 */
    private String name;

    /** 角色: STUDENT, TEACHER, ADMIN, LIBRARIAN */
    private String role;

    /** 联系邮箱 */
    private String email;

    /** 头像链接 */
    private String avatar;

    /** 状态: 1正常, 0禁用 */
    private Integer status;

    /** 逻辑删除 */
    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
