package com.github.opensharing.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


public class RabbitMqProvider {
    public static void main(String[] args) {
        normal_131();


//        error();
    }

/*    private static void error(){
        for (int i = 0; i < 5; i++) {
            try{
                CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
                connectionFactory.setAddresses("127.0.0.1");
                connectionFactory.setPort(8888);
                connectionFactory.setUsername("admin");
                connectionFactory.setPassword("admin");
                connectionFactory.setVirtualHost("/");
                // 如果需要confirm则设置为true
                connectionFactory.setPublisherConfirms(true);
                RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
                MessageProperties messageProperties = new MessageProperties();
                Message message = new Message("高可用测试".getBytes(),messageProperties);
                rabbitTemplate.send("test","test",message);
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }*/

    private static void normal_131(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("192.168.146.131");
        connectionFactory.setPort(8004);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        // 如果需要confirm则设置为true
        connectionFactory.setPublisherConfirms(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        MessageProperties messageProperties = new MessageProperties();
        Message message = new Message("高可用测试".getBytes(),messageProperties);
        while(true){
            try {
                rabbitTemplate.send("test","test",message);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
