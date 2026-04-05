package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRole {
    private Long userId;
    private Long roleId;
    private String roleName;
    private String roleCode;
    private LocalDateTime createTime;
}
