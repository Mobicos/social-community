package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.UserFollowing;
import org.example.model.UserInfo;
import org.example.service.UserFollowingService;
import org.example.service.UserService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/userFollowing")
public class UserFollowingController {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private UserService userService;

    @PostMapping
    public JsonResponse<String> addUserFollowing(@RequestBody UserFollowing userFollowing) {
        Long userId = userTokenService.getUserIdFromToken();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/{followingId}")
    public JsonResponse<String> unfollowUser(@PathVariable Long followingId) {
        Long userId = userTokenService.getUserIdFromToken();
        userFollowingService.unfollowUser(userId, followingId);
        return JsonResponse.success("取消关注成功");
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

    /**
     * 获取推荐用户
     */
    @GetMapping("/recommend")
    public JsonResponse<List<UserInfo>> getRecommendUsers() {
        // TODO: 实现推荐逻辑，目前返回空列表
        return new JsonResponse<>(new ArrayList<>());
    }
}