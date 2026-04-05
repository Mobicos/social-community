package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.UserInfo;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserInfoDao {
    UserInfo getUserInfoById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    int insertUserInfo(UserInfo userInfo);

    int updateUserInfo(UserInfo userInfo);

    int deleteUserInfoById(Long id);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIds);
}
