package com.xingyou.mapper;

import com.xingyou.entity.people.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    int insert(User user);
    
    User findByUserId(@Param("userId") String userId);
    
    int update(User user);
}
