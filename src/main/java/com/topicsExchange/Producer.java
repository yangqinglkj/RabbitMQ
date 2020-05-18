package com.topicsExchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.utils.ConnectionUtil;

/**
 * @author yangqing
 * @since 2020/5/18  17:27
 * 通配符模式（主题模式）（Topics）
 * 生产者
 **/
public class Producer {
    private static final String CHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws Exception{
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建交换机
        channel.exchangeDeclare(CHANGE_NAME, "topic");
        //发送消息
        String msg = "商品.....";
        channel.basicPublish(CHANGE_NAME,"goods.12",null,msg.getBytes());

        System.out.println("Send：" + msg);
        //关闭资源
        channel.close();
        connection.close();
    }
}
