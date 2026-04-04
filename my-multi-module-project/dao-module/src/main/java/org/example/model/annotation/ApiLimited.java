package org.example.model.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ApiLimited {

    /**
     * 角色编码列表：默认是空数组，即不限制角色，表示所有角色都可以访问，
     * 若设置了limitedRoleCodeList，则只有在该数组中的角色才不可以访问，类似于黑名单
     *
     * @return 角色编码数组
     */
    String[] limitedRoleCodeList() default {};

    /**
     *  动态角色键，若设置了roleKey，则根据该键从redis中获取角色编码列表，
     *  若设置了limitedRoleCodeList，则该值无效
     *
     * @return
     */
    String roleKey() default "";
}
