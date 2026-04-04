package org.example.service.auth;

import org.example.dao.UserRoleDao;
import org.example.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleDao userRoleDao;

    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userRoleDao.getUserRoleByUserId(userId);
    }
}
