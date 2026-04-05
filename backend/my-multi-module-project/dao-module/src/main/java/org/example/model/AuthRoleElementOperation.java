package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthRoleElementOperation {
    private Long roleId;
    private Long elementOperationId;
    private LocalDateTime createTime;
    private AuthElementOperation authElementOperation;
}
