package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DlcProducer {

  private static final String EXCHANGE_NAME = "dlx-direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queueName = "laoban_dlx_exchange";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "laoban");

        String queueName2 = "waibao_dlx_exchange";
        channel.queueDeclare(queueName2, true, false, false, null);
        channel.queueBind(queueName2, EXCHANGE_NAME, "waibao");

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String userInput = sc.nextLine();
            String[] strings = userInput.split(" ");
            if (strings.length < 1) {
                continue;
            }
            String message = strings[0];
            String routingKey = strings[1];
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "with routingKey：" + routingKey + "'");
        }

    }
  }
}