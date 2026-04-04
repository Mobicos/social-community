package org.example.service.auth;

import org.example.model.exception.ConditionException;
import org.example.service.UserService;
import org.example.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserTokenService {

    @Autowired
    private UserService userService;

    /**
     * 从请求头中获取 Token 并验证，返回当前用户的 ID。
     *
     * @return 当前用户的 ID
     * @throws ConditionException 如果 Token 无效或不存在
     */
    public Long getUserIdFromToken() {
        // 获取当前请求的上下文
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new ConditionException("请求上下文为空");
        }

        // 获取 HttpServletRequest
        HttpServletRequest request = requestAttributes.getRequest();

        if (request == null) {
            throw new ConditionException("请求对象为空");
        }

        // 从请求头中获取 Token
        String token = request.getHeader("token");

        if (token == null || token.trim().isEmpty()) {
            throw new ConditionException("Token 不存在");
        }

        // 验证 Token 并获取用户 ID
        Long userId = TokenUtil.verifyToken(token);

        if (userId == null || userId < 0) {
            throw new ConditionException("Token 验证失败");
        }
        //双token验证
        this.verifyRefreshToken(userId);
        return userId;
    }

    //验证刷新令牌
    private void verifyRefreshToken(Long userId) {
        // 从请求头中获取refreshToken
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ConditionException("请求上下文无效！");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        String refreshToken = request.getHeader("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ConditionException("refreshToken缺失！");
        }

        // 从数据库中获取用户的refreshToken
        String dbRefreshToken = userService.getRefreshTokenByUserId(userId);
        if (dbRefreshToken == null || !dbRefreshToken.equals(refreshToken)) {
            throw new ConditionException("非法用户！");
        }
    }
}
