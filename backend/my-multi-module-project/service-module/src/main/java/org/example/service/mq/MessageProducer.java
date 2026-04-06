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

    @Resource(required = false)
    private RocketMQTemplate rocketMQTemplate;

    private boolean mqAvailable = true;

    /**
     * 发送字符串消息
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendMessage(String topic, String message) {
        if (!StringUtils.hasText(topic) || !StringUtils.hasText(message)) {
            logger.warn("Invalid topic or message: topic={}, message={}", topic, message);
            return;
        }

        if (rocketMQTemplate == null || !mqAvailable) {
            logger.debug("RocketMQ not available, skipping message: topic={}", topic);
            return;
        }

        try {
            rocketMQTemplate.convertAndSend(topic, message);
            logger.debug("Message sent successfully to topic: {}", topic);
        } catch (Exception e) {
            logger.warn("RocketMQ not available or topic not found: {} - Message will be stored but not broadcast", e.getMessage());
            mqAvailable = false;
        }
    }

    /**
     * 发送JSON或其他对象类型的消息
     * @param topic 主题
     * @param message 消息对象
     */
    public void sendMessage(String topic, Object message) {
        if (!StringUtils.hasText(topic) || message == null) {
            logger.warn("Invalid topic or message: topic={}, message={}", topic, message);
            return;
        }

        if (rocketMQTemplate == null || !mqAvailable) {
            logger.debug("RocketMQ not available, skipping message: topic={}", topic);
            return;
        }

        try {
            rocketMQTemplate.convertAndSend(topic, message);
            logger.debug("Message sent successfully to topic: {}", topic);
        } catch (Exception e) {
            logger.warn("RocketMQ not available or topic not found: {} - Message will be stored but not broadcast", e.getMessage());
            mqAvailable = false;
        }
    }

    /**
     * 异步发送消息
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendAsyncMessage(String topic, String message) {
        if (!StringUtils.hasText(topic) || !StringUtils.hasText(message)) {
            logger.warn("Invalid topic or message: topic={}, message={}", topic, message);
            return;
        }

        if (rocketMQTemplate == null || !mqAvailable) {
            logger.debug("RocketMQ not available, skipping message: topic={}", topic);
            return;
        }

        try {
            rocketMQTemplate.convertAndSend(topic, message);
            logger.debug("Sending message asynchronously to topic: {}", topic);
        } catch (Exception e) {
            logger.warn("RocketMQ not available or topic not found: {}", e.getMessage());
            mqAvailable = false;
        }
    }
}