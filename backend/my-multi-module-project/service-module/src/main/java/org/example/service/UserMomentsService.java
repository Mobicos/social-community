package org.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.model.UserMoment;
import org.example.dao.UserMomentsDao;
import org.example.model.constant.UserMomentConstant;
import org.example.service.mq.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户动态业务逻辑层
 */
@Service
public class UserMomentsService {

    @Autowired
    private UserMomentsDao userMomentsDao;

    // 注入MessageProducer
    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void addUserMoment(UserMoment userMoment) {
        userMoment.setCreateTime(LocalDateTime.now());
        userMomentsDao.insert(userMoment);

        // 调用sendMessage方法发送消息
        messageProducer.sendMessage(UserMomentConstant.MOMENT_TOPIC, JSONObject.toJSONString(userMoment));
    }

    // 从 Redis List 中读取数据并反序列化为 List<UserMoment>
    public List<UserMoment> getUpMomentsFromRedis(Long userId) {
        String key = String.format("subscribed-%d", userId);
        return redisTemplate.execute((RedisCallback<List<UserMoment>>) connection -> {
            // 获取 List 中的所有元素（从索引 0 到 -1 表示获取整个 List）
            List<byte[]> userMomentsBytes = connection.lRange(key.getBytes(StandardCharsets.UTF_8), 0, -1);
            List<UserMoment> userMoments = new ArrayList<>();
            for (byte[] bytes : userMomentsBytes) {
                String json = new String(bytes, StandardCharsets.UTF_8);
                userMoments.add(JSONObject.parseObject(json, UserMoment.class));
            }
            return userMoments;
        });
    }
}