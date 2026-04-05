package org.example.dao;

import org.example.model.UserMoment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户动态数据访问层
 */
@Mapper
public interface UserMomentsDao {

    /**
     * 插入用户动态
     * @param userMoment 用户动态对象
     * @return 影响的行数
     */
    int insert(UserMoment userMoment);
}