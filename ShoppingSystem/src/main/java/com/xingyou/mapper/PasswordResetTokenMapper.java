package com.xingyou.mapper;

import com.xingyou.entity.people.PasswordResetToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 密码重置Token数据访问层
 */
@Mapper
public interface PasswordResetTokenMapper {
    
    /**
     * 插入重置Token
     *
     * @param token 重置Token对象
     * @return 影响行数
     */
    int insert(PasswordResetToken token);
    
    /**
     * 根据Token查询
     *
     * @param token Token字符串
     * @return 重置Token对象
     */
    PasswordResetToken findByToken(@Param("token") String token);
    
    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 重置Token对象
     */
    PasswordResetToken findByUserId(@Param("userId") String userId);
    
    /**
     * 标记Token为已使用
     *
     * @param token Token字符串
     * @return 影响行数
     */
    int markAsUsed(@Param("token") String token);
    
    /**
     * 删除Token
     *
     * @param token Token字符串
     * @return 影响行数
     */
    int deleteByToken(@Param("token") String token);
}
