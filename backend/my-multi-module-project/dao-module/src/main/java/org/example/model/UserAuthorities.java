package org.example.model;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder // 添加 @Builder 注解
public class UserAuthorities {
    private List<AuthRoleElementOperation> roleElementOperationList;
    private List<AuthRoleMenu> roleMenuList;
}
