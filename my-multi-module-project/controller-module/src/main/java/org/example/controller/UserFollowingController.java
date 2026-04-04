package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.UserFollowing;
import org.example.service.UserFollowingService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userFollowing")
public class UserFollowingController {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping
    public JsonResponse<String> addUserFollowing(@RequestBody UserFollowing userFollowing) {
        Long userId = userTokenService.getUserIdFromToken();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }
}