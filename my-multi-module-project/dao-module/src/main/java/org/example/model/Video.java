package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {
    /**
     * 视频的唯一标识符
     */
    private String id;

    /**
     * 视频的标题
     */
    private String title;

    /**
     * 视频的描述
     */
    private String description;

    /**
     * 视频的分类（如技术教程、面试准备、架构设计等）
     */
    private String category;

    /**
     * 视频的发布日期
     */
    private String publish_date;

    /**
     * 视频的时长（单位：分钟）
     */
    private int duration;

    /**
     * 视频的播放量
     */
    private long views;

    /**
     * 视频的评论数
     */
    private long comments;

    /**
     * 视频的点赞数
     */
    private long likes;

    /**
     * 视频的收藏数
     */
    private long favorites;

    // Getters and Setters
}
