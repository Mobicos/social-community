package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.auth.UserTokenService;
import org.example.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/users") // 统一前缀，便于管理
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 用户注册
     */
    @PostMapping
    public JsonResponse<String> register(@RequestBody User user) {
        userService.register(user);
        return JsonResponse.success("用户注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    /**
     * 获取 RSA 公钥：客户端可以通过 HTTP GET 请求获取公钥，并使用公钥加密用户的密码，然后将加密后的密码发送给服务器。
     */
    @GetMapping("/rsa-public-key")
    public JsonResponse<String> getRsaPublicKey() {
        String publicKey = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKey);
    }


    /**
     * 获取当前用户信息：
     * 从请求头中提取 Token，验证 Token 的有效性，并返回当前用户的详细信息。
     *
     * @return JsonResponse<User> 包含用户信息的响应对象
     */
    @GetMapping
    public JsonResponse<User> getCurrentUserInfo() {
        // 从 Token 中获取当前用户 ID
        Long userId = userTokenService.getUserIdFromToken();

        // 根据用户 ID 获取用户信息
        User user = userService.getUserInfo(userId);

        // 返回用户信息
        return new JsonResponse<>(user);
    }

    /**
     * 双token登录：同时获取访问令牌和刷新令牌
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/login-dts")
    public JsonResponse<Map<String, Object>> loginDts(@RequestBody User user) throws Exception {
        Map<String, Object> map = userService.loginDts(user);
        return new JsonResponse<>(map);
    }


    @DeleteMapping("/logout")
    public JsonResponse<String> logout(HttpServletRequest request){
        Long userId = userTokenService.getUserIdFromToken();
        userService.logout(userId);
        return JsonResponse.success();
    }

    /**
     * 刷新访问令牌：
     * 1. 从请求头中获取刷新令牌
     * 2. 验证刷新令牌是否有效
     * 3. 如果刷新令牌有效，则生成新的访问令牌
     * 4. 返回新的访问令牌
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/refresh-access-token")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }
}