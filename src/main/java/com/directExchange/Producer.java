package com.directExchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.utils.ConnectionUtil;

/**
 * @author yangqing
 * @since 2020/5/18  16:22
 * 路由模式 Routing
 * 生产者
 **/
public class Producer {
    private static final String CHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(CHANGE_NAME, "direct");

        //发送消息
        String msg = "Routing direct";

        //路由key
        String routingKey = "error";
        channel.basicPublish(CHANGE_NAME, routingKey, null, msg.getBytes());

        System.out.println("Send：" + msg);
        //关闭资源
        channel.close();
        connection.close();

    }
}
