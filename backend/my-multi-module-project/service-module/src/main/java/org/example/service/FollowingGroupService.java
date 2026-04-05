package org.example.service;

import org.example.dao.FollowingGroupDao;
import org.example.model.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingGroupService {
    @Autowired
    private FollowingGroupDao followingGroupDao;

    public List<FollowingGroup> getGroupsByUserId(Long userId) {
        return followingGroupDao.getGroupsByUserId(userId);
    }

    public int insertFollowingGroup(FollowingGroup group) {
        return followingGroupDao.insertFollowingGroup(group);
    }

    public int updateFollowingGroup(FollowingGroup group) {
        return followingGroupDao.updateFollowingGroup(group);
    }

    public int deleteFollowingGroup(Long id) {
        return followingGroupDao.deleteFollowingGroup(id);
    }

    public FollowingGroup getByGroupType(String groupType){
        return followingGroupDao.getByGroupType(groupType);
    }

    public FollowingGroup getByGroupId(Long id){
        return followingGroupDao.getByGroupId(id);
    }
}