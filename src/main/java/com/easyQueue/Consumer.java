package com.easyQueue;

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.utils.ConnectionUtil;

import java.io.IOException;

/**
 * @author yangqing
 * @since 2020/5/12  16:55
 * 消费者
 **/
public class Consumer {
    private static final String QUEUE_NAME = "test_easy_queue";

    public static void main(String[] args) throws Exception {
        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //获取监听到的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body, "UTF-8");
                System.out.println("消费消息:" + msgString);
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }


    private static void oldMethod() throws IOException, InterruptedException {
        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            //拿到一个消息的包装体
            Delivery delivery = consumer.nextDelivery();
            String msgString = new String(delivery.getBody());
            System.out.println("消费消息:" + msgString);
        }
    }
}
