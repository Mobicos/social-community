package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.AuthRoleElementOperation;
import org.example.model.AuthRoleMenu;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleDao {
    List<AuthRoleElementOperation> getAuthRoleElementOperationsByRoleIds(@Param("roleIds") Set<Long> roleIds);
    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
