package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowingGroup {
    private Long id;
    private Long userId;
    private String groupName;
    private String groupType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
