package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param exchange   交换机
     * @param routingKey 路由密钥
     * @param message    消息
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
