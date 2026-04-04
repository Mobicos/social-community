package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.UserRole;

import java.util.List;

@Mapper
public interface UserRoleDao {
    List<UserRole> getUserRoleByUserId(@Param("userId") Long userId);
}
