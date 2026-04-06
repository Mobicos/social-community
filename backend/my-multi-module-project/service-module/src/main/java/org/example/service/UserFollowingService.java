package org.example.service;

import org.example.dao.UserFollowingDao;
import org.example.model.FollowingGroup;
import org.example.model.User;
import org.example.model.UserFollowing;
import org.example.model.UserInfo;
import org.example.model.constant.UserConstant;
import org.example.model.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    /**
     * 添加关注
     * 逻辑流程：
     * 1. 检查关注分组是否存在（如果用户未指定分组，则自动分配到默认分组）。
     * 2. 检查被关注的用户是否存在。
     * 3. 删除已存在的关注记录（避免重复关注）。
     * 4. 创建新关注记录。
     *
     * @param userFollowing 用户关注信息
     */
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        // 设置关注分组
        setFollowingGroup(userFollowing);

        // 检查被关注的用户是否存在
        checkUserExists(userFollowing.getFollowingId());

        // 删除已存在的关注记录（避免重复关注）
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), userFollowing.getFollowingId());

        // 添加关注记录
        userFollowingDao.insertUserFollowing(userFollowing);
    }

    private void setFollowingGroup(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();
        if (groupId == null) {
            FollowingGroup defaultGroup = followingGroupService.getByGroupType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            if (defaultGroup == null) {
                throw new ConditionException("默认关注分组不存在！");
            }
            userFollowing.setGroupId(defaultGroup.getId());
        } else {
            FollowingGroup group = followingGroupService.getByGroupId(groupId);
            if (group == null) {
                throw new ConditionException("关注分组不存在！");
            }
        }
    }

    private void checkUserExists(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ConditionException("关注的用户不存在！");
        }
    }

    /**
     * 获取用户的粉丝列表
     * 逻辑流程：
     * 1. 获取当前用户的粉丝列表。
     * 2. 提取粉丝的用户ID集合。
     * 3. 获取粉丝的用户信息列表。
     * 4. 将用户信息列表转换为 Map，便于快速查找。
     * 5. 获取当前用户的关注列表。
     * 6. 提取关注的用户ID集合。
     * 7. 填充粉丝的用户信息，并标记是否被当前用户关注。
     *
     * @param userId
     * @return
     */
    public List<UserFollowing> getUserFans(Long userId) {
        // 获取当前用户的粉丝列表
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);

        // 提取粉丝的用户ID集合
        Set<Long> fanIdSet = fanList.stream()
                .map(UserFollowing::getUserId)
                .collect(Collectors.toSet());

        // 获取粉丝的用户信息列表
        List<UserInfo> userInfoList = new ArrayList<>();
        if (!fanIdSet.isEmpty()) {
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }

        // 将用户信息列表转换为 Map，便于快速查找
        Map<Long, UserInfo> userInfoMap = userInfoList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));

        // 获取当前用户的关注列表
        List<UserFollowing> followingList = userFollowingDao.getFollowingsByUserId(userId);
        Set<Long> followingIdSet = followingList.stream()
                .map(UserFollowing::getFollowingId)
                .collect(Collectors.toSet());

        // 填充粉丝的用户信息，并标记是否被当前用户关注
        for (UserFollowing fan : fanList) {
            UserInfo userInfo = userInfoMap.get(fan.getUserId());
            if (userInfo != null) {
                fan.setUserInfo(userInfo); // 设置用户信息
                fan.getUserInfo().setFollowed(followingIdSet.contains(fan.getUserId())); // 标记是否被关注
            }
        }

        return fanList;
    }

    /**
     * 获取用户的关注列表
     *
     * @param userId 用户ID
     * @return 关注列表
     */
    public List<UserFollowing> getFollowings(Long userId) {
        // 获取当前用户的关注列表
        List<UserFollowing> followingList = userFollowingDao.getFollowingsByUserId(userId);

        // 提取关注的用户ID集合
        Set<Long> followingIdSet = followingList.stream()
                .map(UserFollowing::getFollowingId)
                .collect(Collectors.toSet());

        // 获取被关注用户的用户信息列表
        List<UserInfo> userInfoList = new ArrayList<>();
        if (!followingIdSet.isEmpty()) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }

        // 将用户信息列表转换为 Map，便于快速查找
        Map<Long, UserInfo> userInfoMap = userInfoList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));

        // 获取当前用户的粉丝列表，用于判断是否互相关注
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);
        Set<Long> fanIdSet = fanList.stream()
                .map(UserFollowing::getUserId)
                .collect(Collectors.toSet());

        // 填充关注的用户信息，并标记是否互相关注
        for (UserFollowing following : followingList) {
            UserInfo userInfo = userInfoMap.get(following.getFollowingId());
            if (userInfo != null) {
                following.setUserInfo(userInfo);
                // 如果粉丝列表中也包含该用户，则表示互相关注
                following.getUserInfo().setFollowed(fanIdSet.contains(following.getFollowingId()));
            }
        }

        return followingList;
    }

    /**
     * 取消关注
     *
     * @param userId      当前用户ID
     * @param followingId 被取消关注的用户ID
     */
    public void unfollowUser(Long userId, Long followingId) {
        userFollowingDao.deleteUserFollowing(userId, followingId);
    }
}