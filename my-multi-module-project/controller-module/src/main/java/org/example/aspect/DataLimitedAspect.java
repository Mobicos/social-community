package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.model.UserMoment;
import org.example.model.UserRole;
import org.example.model.constant.AuthRoleConstant;
import org.example.model.exception.ConditionException;
import org.example.service.auth.UserRoleService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Aspect
public class DataLimitedAspect {

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 定义切入点，匹配带有@DataLimited注解的方法。
     */
    @Pointcut("@annotation(org.example.model.annotation.DataLimited)")
    public void check() {
    }

    /**
     * 在切入点方法执行前执行的逻辑。
     * 检查用户的角色权限，限制某些操作。
     */
    @Before("check()")
    public void doBefore(JoinPoint joinPoint) {
        // 从Token中获取当前用户的ID
        Long userId = userTokenService.getUserIdFromToken();
        if (userId == null) {
            throw new ConditionException("未获取到有效的用户ID");
        }

        // 获取用户的角色列表，并提取角色代码
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream()
                .map(UserRole::getRoleCode)
                .collect(Collectors.toSet());

        // 检查方法参数，判断是否符合权限要求
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserMoment) {
                UserMoment userMoment = (UserMoment) arg;
                Integer type = userMoment.getType();

                // 检查用户是否拥有ROLE_P5角色，并且type不为0（视频）
                if (roleCodeSet.contains(AuthRoleConstant.ROLE_P9) && !"0".equals(type)) {
                    throw new ConditionException("用户权限不足，仅允许操作类型为视频（type=0）");
                }
            }
        }
    }
}
