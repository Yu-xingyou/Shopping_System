package com.xingyou.mapper;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper  {
    
    List<User> findAllUsers();
    
    User findUserByUserId(@Param("userId") String userId);
    
    List<User> findUserByName(@Param("name") String name);
    
    List<Staff> findAllStaffs();
    
    Staff findStaffByStaffId(@Param("staffId") Integer staffId);
    
    List<Staff> findStaffByName(@Param("name") String name);
    
    int insertStaff(Staff staff);
    
    int updateStaff(Staff staff);
    
    int updateProductStock(@Param("id") Integer id, @Param("stock") Integer stock);
}
