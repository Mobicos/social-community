package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.UserFollowing;
import org.example.service.UserFollowingService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 获取当前用户的关注列表
     */
    @GetMapping("/following")
    public JsonResponse<List<UserFollowing>> getFollowingList() {
        Long userId = userTokenService.getUserIdFromToken();
        List<UserFollowing> followingList = userFollowingService.getFollowings(userId);
        return new JsonResponse<>(followingList);
    }

    /**
     * 获取当前用户的粉丝列表
     */
    @GetMapping("/followers")
    public JsonResponse<List<UserFollowing>> getFollowersList() {
        Long userId = userTokenService.getUserIdFromToken();
        List<UserFollowing> fansList = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(fansList);
    }
}