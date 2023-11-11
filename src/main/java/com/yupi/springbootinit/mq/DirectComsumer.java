package com.yupi.springbootinit.mq;

import com.rabbitmq.client.*;

public class DirectComsumer {

  private static final String EXCHANGE_NAME = "direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    // 创建队列，随机分配一个队列名称
    String queueName = "direct1工作队列";
    channel.queueDeclare(queueName, true, false, false, null);
    channel.queueBind(queueName, EXCHANGE_NAME, "xiaoli");
    String queueName2 = "direct2工作队列";
    channel.queueDeclare(queueName2, true, false, false, null);
    channel.queueBind(queueName2, EXCHANGE_NAME, "xiaopi");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaoli] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaopi] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName, true, deliverCallback1, consumerTag -> { });
    channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
  }
}