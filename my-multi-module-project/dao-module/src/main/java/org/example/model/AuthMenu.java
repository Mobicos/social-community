package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthMenu {

    private Long id;

    private String name;

    private String code;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
