package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户动态实体类
 */
@Data
public class UserMoment {
    private Long id; // 主键ID
    private Long userId; // 用户ID
    private Integer type; // 动态类型：0-视频, 1-直播, 2-专栏动态
    private Long contentId; // 内容详情ID
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}