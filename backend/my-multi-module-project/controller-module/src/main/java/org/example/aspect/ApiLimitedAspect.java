package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.model.UserRole;
import org.example.model.annotation.ApiLimited;
import org.example.model.exception.ConditionException;
import org.example.service.auth.UserRoleService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ApiLimitedAspect {

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(org.example.model.annotation.ApiLimited)")
    public void check() {
    }

    @Before("check() && @annotation(apiLimited)")
    public void doBefore(JoinPoint joinPoint, ApiLimited apiLimited) {
        // 获取当前登录用户的ID
        Long userId = userTokenService.getUserIdFromToken();

        // 获取当前用户的角色列表
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);

        // 获取注解中的限制角色并转换为Set
        Set<String> limitedRoleCodeSet = new HashSet<>(Arrays.asList(apiLimited.limitedRoleCodeList()));

        // 获取注解中的角色Key，并从缓存中获取限制角色列表
//        String roleKey = apiLimited.roleKey();
//        String[] limitedRoleCodeList = userRoleService.getLimitedRoles(roleKey);

        // 获取用户角色并转换为Set
        Set<String> userRoleCodeSet = userRoleList.stream()
                .map(UserRole::getRoleCode)
                .collect(Collectors.toSet());

        // 检查用户角色是否与限制角色有交集
        if (!Collections.disjoint(userRoleCodeSet, limitedRoleCodeSet)) {
            throw new ConditionException("权限不足！");
        }
    }
}
