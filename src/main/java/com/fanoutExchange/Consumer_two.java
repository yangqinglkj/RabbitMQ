package com.fanoutExchange;

import com.rabbitmq.client.*;
import com.utils.ConnectionUtil;

import java.io.IOException;

/**
 * @author yangqing
 * @since 2020/5/13  17:25
 * 订阅模式
 * 消费者
 **/
public class Consumer_two {
    private static final String QUEUE_NAME="test_queue_fanout_sms";
    private static final String CHANGE_NAME="test_exchange_fanout";

    public static void main(String[] args)throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        final Channel channel = connection.createChannel();
        //声明队列
       channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,CHANGE_NAME, "");

        //同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //定义队列的消费者
        Consumer consumer = new DefaultConsumer(channel) {
            //获取监听到的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body, "UTF-8");
                System.out.println("消费者two： " + msgString);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //手动确认模式
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //监听队列
        //autoAck true代表自动返回完成状态  false代表手动返回完成状态
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
