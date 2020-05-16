package com.publicshSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.utils.ConnectionUtil;

/**
 * @author yangqing
 * @since 2020/5/13  17:14
 * 订阅模式
 * 生产者
 **/
public class Producer {

    private static final String CHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args)throws Exception{
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(CHANGE_NAME,"fanout");//分发类型

        String msg = "订阅模式生产者";
        //发送消息
        channel.basicPublish(CHANGE_NAME,"",null,msg.getBytes());

        System.out.println("订阅模式生产者发送消息: "+msg);

        //关闭资源
        channel.close();
        connection.close();
    }

}
