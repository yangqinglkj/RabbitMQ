package com;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.utils.ConnectionUtil;

import java.io.IOException;

/**
 * @author yangqing
 * @since 2020/5/21  13:58
 * confirm确认消息
 **/
public class ConfirmProducer {
    private static final String EXCHANGE_NAME = "test_confirm_exchange";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);
        String routingKey = "item.update";
        //指定消息的投递格式：confirm模式
        channel.confirmSelect();
        //发送
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 5 ; i++) {
            String msg = "this is confirm msg ";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
            System.out.println("Send message : " + msg);
        }
        //添加一个确认监听， 这里就不关闭连接了，为了能保证能收到监听消息
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 返回成功的回调函数
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("success ack");
                System.out.println(deliveryTag);
                System.out.println(multiple);
                System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
            }
            /**
             * 返回失败的回调函数
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("defeat ack");
                System.out.println(deliveryTag);
                System.out.println(multiple);
                System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
            }
        });
    }
}
