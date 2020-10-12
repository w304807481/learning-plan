package com.github.opensharing.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMqCustomer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.146.131");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setConnectionTimeout(50000);
        // 设置服务端的地址、端口、用户名和密码...

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("test", BuiltinExchangeType.TOPIC, true);
        channel.queueDeclare("test", true, false, false, null);
        channel.queueBind("test","test","");
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body);
                System.out.println("Received: " + message);
                // 消息确认
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume("test",false,consumer);
    }
}
