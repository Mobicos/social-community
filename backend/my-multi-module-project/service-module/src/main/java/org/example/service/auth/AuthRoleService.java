package org.example.service.auth;

import org.example.dao.AuthRoleDao;
import org.example.model.AuthRoleElementOperation;
import org.example.model.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class AuthRoleService {
    @Autowired
    private AuthRoleDao authRoleDao;

    public List<AuthRoleElementOperation> getAuthRoleElementOperationsByRoleIds(Set<Long> roleIds) {
        return authRoleDao.getAuthRoleElementOperationsByRoleIds(roleIds);
    }

    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIds) {
        return authRoleDao.getAuthRoleMenusByRoleIds(roleIds);
    }
}
