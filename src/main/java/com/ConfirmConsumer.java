package com;

import com.rabbitmq.client.*;
import com.utils.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author yangqing
 * @since 2020/5/21  14:05
 **/
public class ConfirmConsumer {
    private static final String EXCHANGE_NAME = "test_confirm_exchange";
    private static final String QUEUE_NAME = "test_confirm_queue";

    public static void main(String[] args)throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        String routingKey = "item.#";
        //将队列绑定到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,routingKey);
        //创建消费者并接收消息
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        //设置 Channel 消费者绑定队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
