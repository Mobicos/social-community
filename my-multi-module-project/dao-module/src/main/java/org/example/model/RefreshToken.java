package org.example.model;

import lombok.Data;

import java.util.Date;

@Data
public class RefreshToken {
    private Long id;

    private String refreshToken;

    private Long userId;

    private Date createTime;
}
