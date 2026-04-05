package org.example.service.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.example.model.UserFollowing;
import org.example.model.UserMoment;
import org.example.model.constant.UserMomentConstant;
import org.example.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RocketMQMessageListener(topic = UserMomentConstant.MOMENT_TOPIC, consumerGroup = UserMomentConstant.MOMENT_GROUP)
public class MessageConsumer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserFollowingService userFollowingService;

    @Override
    public void onMessage(String message) {
        try {
            // 模拟消息处理逻辑
            logger.info("Received message: {}", message);
            processMessage(message);
        } catch (Exception e) {
            logger.error("Failed to process message: {}", message, e);
        }
    }


    /**
     * 消息处理逻辑：推送给粉丝
     * @param message 消息内容
     */
    private void processMessage(String message) {
        try {
            if (message == null || message.trim().isEmpty()) {
                logger.warn("Received empty or null message");
                return;
            }

            // 解析消息为 UserMoment 对象
            UserMoment userMoment = JSONObject.parseObject(message, UserMoment.class);
            if (userMoment == null) {
                logger.warn("Failed to parse message: {}", message);
                return;
            }
            Long userId = userMoment.getUserId();

            // 获取粉丝列表
            List<UserFollowing> fanList = userFollowingService.getUserFans(userId);
            if (fanList == null || fanList.isEmpty()) {
                logger.info("No fans found for user: {}", userId);
                return;
            }

            // 准备 Redis 操作所需的数据
            byte[] userMomentBytes = JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8);

            // 使用 Redis Pipeline 批量执行 leftPush 操作
            redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
                for (UserFollowing fan : fanList) {
                    String key = "subscribed-" + fan.getUserId();
                    connection.lPush(key.getBytes(StandardCharsets.UTF_8), userMomentBytes);
                }
                return null;
            });

            // 日志记录
            logger.info("Processed message: {}", message);
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }

}