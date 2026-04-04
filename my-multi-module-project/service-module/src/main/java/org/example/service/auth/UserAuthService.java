package org.example.service.auth;

import org.example.model.AuthRoleElementOperation;
import org.example.model.AuthRoleMenu;
import org.example.model.UserAuthorities;
import org.example.model.UserRole;
import org.example.model.annotation.LoggingMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthRoleService authRoleService;

    @LoggingMonitor // 添加日志记录注解
    public UserAuthorities getUserAuthorities(Long userId) {
        // 获取用户角色列表
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        if (userRoleList.isEmpty()) {
            return UserAuthorities.builder().build(); // 使用 builder 构建空对象
        }
        // 提取角色ID集合
        Set<Long> roleIds = userRoleList.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
        // 获取角色对应的元素操作权限列表
        List<AuthRoleElementOperation> roleElementOperationList = authRoleService.getAuthRoleElementOperationsByRoleIds(roleIds);
        // 获取角色对应的菜单权限列表
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenusByRoleIds(roleIds);
        // 构建用户权限对象并返回
        return UserAuthorities.builder()
                .roleElementOperationList(roleElementOperationList)
                .roleMenuList(authRoleMenuList)
                .build();
    }
}
