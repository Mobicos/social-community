package org.example;

import org.example.service.mq.MessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RocketMQTest {

    @Autowired
    private MessageProducer producer;

    @Test
    public void testSendMessage() {
        producer.sendMessage("testTopic", "Hello RocketMQ");
    }
}