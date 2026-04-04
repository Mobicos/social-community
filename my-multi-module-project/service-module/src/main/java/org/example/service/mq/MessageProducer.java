package org.example.service.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送字符串消息
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendMessage(String topic, String message) {
        if (!StringUtils.hasText(topic) || !StringUtils.hasText(message)) {
            logger.error("Invalid topic or message: topic={}, message={}", topic, message);
            return;
        }

        try {
            rocketMQTemplate.convertAndSend(topic, message);
            logger.info("Message sent successfully to topic: {}, message: {}", topic, message);
        } catch (Exception e) {
            logger.error("Failed to send message to topic: {}, message: {}", topic, message, e);
        }
    }

    /**
     * 发送JSON或其他对象类型的消息
     * @param topic 主题
     * @param message 消息对象
     */
    public void sendMessage(String topic, Object message) {
        if (!StringUtils.hasText(topic) || message == null) {
            logger.error("Invalid topic or message: topic={}, message={}", topic, message);
            return;
        }

        try {
            rocketMQTemplate.convertAndSend(topic, message);
            logger.info("Message sent successfully to topic: {}, message: {}", topic, message);
        } catch (Exception e) {
            logger.error("Failed to send message to topic: {}, message: {}", topic, message, e);
        }
    }

    /**
     * 异步发送消息
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendAsyncMessage(String topic, String message) {
        if (!StringUtils.hasText(topic) || !StringUtils.hasText(message)) {
            logger.error("Invalid topic or message: topic={}, message={}", topic, message);
            return;
        }

        rocketMQTemplate.convertAndSend(topic, message, m -> {
            logger.info("Sending message asynchronously to topic: {}, message: {}", topic, message);
            return m;
        });
    }
}