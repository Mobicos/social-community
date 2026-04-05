package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.UserMoment;
import org.example.model.annotation.ApiLimited;
import org.example.model.annotation.DataLimited;
import org.example.model.constant.AuthRoleConstant;
import org.example.service.UserMomentsService;
import org.example.service.auth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userMoment")
public class UserMomentsController {

    @Autowired
    private UserMomentsService userMomentsService;

    @Autowired
    private UserTokenService userTokenService;

    @ApiLimited(limitedRoleCodeList = {AuthRoleConstant.ROLE_P5})
    @DataLimited
    @PostMapping
    public JsonResponse<String> addUserMoment(@RequestBody UserMoment userMoment) {
        Long userId = userTokenService.getUserIdFromToken();
        userMoment.setUserId(userId);
        userMomentsService.addUserMoment(userMoment);
        return JsonResponse.success();
    }

    @GetMapping("/upMoments")
    public JsonResponse<List<UserMoment>> getUpMomentsFromRedis(){
        Long userId = userTokenService.getUserIdFromToken();
        List<UserMoment> list = userMomentsService.getUpMomentsFromRedis(userId);
        return new JsonResponse<>(list);
    }
}