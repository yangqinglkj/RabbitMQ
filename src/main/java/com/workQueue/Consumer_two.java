package com.workQueue;

import com.rabbitmq.client.*;
import com.utils.ConnectionUtil;

import java.io.IOException;

/**
 * @author yangqing
 * @since 2020/5/12  17:42
 * 消费者1
 **/
public class Consumer_two {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        final Channel channel = connection.createChannel();
        //从通道中创建队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //同一时刻服务器只会发一条消息给消费者,需要消费者手动确认，确认后才会发第二条消息
        channel.basicQos(1);
        //定义队列的消费者
        Consumer consumer = new DefaultConsumer(channel) {
            //获取监听到的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body, "UTF-8");
                System.out.println("消费者two" + msgString);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
//                    System.out.println("消费者two处理完");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        boolean autoAck = true;
        //监听队列
        //autoAck true代表自动返回完成状态  false代表手动返回完成状态
        channel.basicConsume(QUEUE_NAME, false, consumer);

    }
}
