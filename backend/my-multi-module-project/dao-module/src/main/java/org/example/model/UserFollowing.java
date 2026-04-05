package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFollowing {
    private Long id;
    private Long userId;
    private Long followingId;
    private Long groupId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private UserInfo userInfo;
}
