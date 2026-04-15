package com.xingyou.entity.people;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 统一用户实体类
 * 通过 role 字段区分不同角色：
 * 0 - 普通用户
 * 1 - 员工
 * 2 - 管理员
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户ID
     * 普通用户: U + 时间戳 + 随机数
     * 员工/管理员: 整数ID
     */
    private String userId;

    /**
     * 用户名/姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别（0-未知，1-男，2-女，3-其他）
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像地址（URL）
     */
    private String avatar;

    /**
     * 余额（仅普通用户使用）
     */
    private BigDecimal money;

    /**
     * 角色类型
     * 0 - 普通用户
     * 1 - 员工
     * 2 - 管理员
     */
    private Integer role;

    /**
     * 状态（仅员工和管理员使用，然后也会用于表示用户是否正常）
     * 1 - 正常
     * 2 - 冻结
     * 3 - 离职（员工）/ 其他（管理员）
     */
    private Integer status;

    /**
     * 获取性别名称
     * @return 性别的中文名称
     */
    public String getSexName() {
        if (sex == null) {
            return "未知";
        }
        switch (sex) {
            case 1:
                return "男";
            case 2:
                return "女";
            case 3:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取角色名称
     * @return 角色的中文名称
     */
    public String getRoleName() {
        if (role == null) {
            return "未知";
        }
        switch (role) {
            case 0:
                return "普通用户";
            case 1:
                return "员工";
            case 2:
                return "管理员";
            default:
                return "未知";
        }
    }

    /**
     * 判断是否为普通用户
     * @return true-是普通用户，false-不是
     */
    public boolean isNormalUser() {
        return role != null && role == 0;
    }

    /**
     * 判断是否为员工
     * @return true-是员工，false-不是
     */
    public boolean isStaff() {
        return role != null && role == 1;
    }

    /**
     * 判断是否为管理员
     * @return true-是管理员，false-不是
     */
    public boolean isAdmin() {
        return role != null && role == 2;
    }
}
