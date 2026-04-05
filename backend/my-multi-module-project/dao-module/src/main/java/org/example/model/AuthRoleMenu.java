package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthRoleMenu {
    private Long roleId;
    private Long menuId;
    private LocalDateTime createTime;
    private AuthMenu authMenu;
}
