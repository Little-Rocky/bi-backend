package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DlxConsumer {

  private static final String EXCHANGE_NAME = "dlx-direct-exchange";

  private static final String EXCHANGE_NAME2 = "direct2-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME2, "direct");

    // 指定死信队列的参数
    Map<String, Object> args = new HashMap<>();
    // 要绑定哪个交换机
    args.put("x-dead-letter-exchange", EXCHANGE_NAME);
    // 指定死信转换到哪个死信队列
    args.put("x-dead-letter-routing-key", "waibao");

    // 创建队列 随机创建一个交换机
    String queueName = "xiaogou_queue";
    channel.queueDeclare(queueName, true, false, false, args);
    channel.queueBind(queueName, EXCHANGE_NAME, "xiaogou");

    // 老板队列
    Map<String, Object> args2 = new HashMap<>();
    args2.put("x-dead-letter-exchange", EXCHANGE_NAME);
    args2.put("x-dead-letter-routing-key", "laoban");

    String queueName2 = "xiaomao_queue";
    channel.queueDeclare(queueName2, true, false, false, args2);
    channel.queueBind(queueName2, EXCHANGE_NAME, "xiaomao");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaoa] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaob] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };

    channel.basicConsume(queueName, true, deliverCallback1, consumerTag -> { });
    channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });


  }
}