package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.UserFollowing;

import java.util.List;

@Mapper
public interface UserFollowingDao {
    List<UserFollowing> getFollowingsByUserId(Long userId);
    int insertUserFollowing(UserFollowing userFollowing);
    int updateUserFollowing(UserFollowing userFollowing);
    int deleteUserFollowing(Long userId, Long followingId);
    List<UserFollowing> getUserFans(Long userId);
}
