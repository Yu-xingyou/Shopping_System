package com.xingyou.mapper;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffMapper {
    
    Staff findById(@Param("staffId") Integer staffId);
    
    Staff findByName(@Param("name") String name);
    
    List<User> findAllUsers();
    
    User findUserByUserId(@Param("userId") String userId);
    
    List<User> findUserByName(@Param("name") String name);
}
