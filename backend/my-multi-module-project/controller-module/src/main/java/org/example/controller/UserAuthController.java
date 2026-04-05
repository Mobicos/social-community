package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.UserAuthorities;
import org.example.service.auth.UserAuthService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-auth") // 统一前缀，便于管理
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserTokenService userTokenService;

    @GetMapping
    public JsonResponse<UserAuthorities> getUserAuthorities() {
        Long userId = userTokenService.getUserIdFromToken();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }

}