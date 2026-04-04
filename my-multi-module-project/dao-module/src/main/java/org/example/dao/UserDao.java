package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.model.RefreshToken;
import org.example.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User getUserById(Long id);

    User getUserByPhone(String phone);

    User getUserByEmail(String email);

    User getUserByPhoneOrEmail(String phone, String email);

    int insertUser(User user);

    int updateUser(User user);

    int deleteUserById(Long id);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("userId") Long userId);

    String getRefreshTokenByUserId(Long userId);

    Integer deleteRefreshTokenByUserId(Long userId);

    RefreshToken getRefreshToken(String refreshToken);
}