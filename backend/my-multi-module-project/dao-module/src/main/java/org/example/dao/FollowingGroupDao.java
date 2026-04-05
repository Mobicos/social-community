package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.FollowingGroup;

import java.util.List;

@Mapper
public interface FollowingGroupDao {
    List<FollowingGroup> getGroupsByUserId(Long userId);
    int insertFollowingGroup(FollowingGroup group);
    int updateFollowingGroup(FollowingGroup group);
    int deleteFollowingGroup(Long id);
    FollowingGroup getByGroupType(String groupType);
    FollowingGroup getByGroupId(Long id);
}
