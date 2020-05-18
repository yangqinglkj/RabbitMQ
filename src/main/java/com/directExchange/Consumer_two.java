package com.directExchange;

import com.rabbitmq.client.*;
import com.utils.ConnectionUtil;

import java.io.IOException;

/**
 * @author yangqing
 * @since 2020/5/18  16:30
 * routing 消费者
 **/
public class Consumer_two {
    private static final String QUEUE_NAME = "test_routing_queue_2";
    private static final String CHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        //绑定交换机
        channel.queueBind(QUEUE_NAME,CHANGE_NAME,"error");
        channel.queueBind(QUEUE_NAME,CHANGE_NAME,"info");
        channel.queueBind(QUEUE_NAME,CHANGE_NAME,"warning");

        //同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //定义一个消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
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
