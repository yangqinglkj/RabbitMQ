package com.easyQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.utils.ConnectionUtil;


/**
 * @author yangqing
 * @since 2020/5/12  16:27
 * 生产者
 **/
public class Producer {

    private static final String QUEUE_NAME = "test_easy_queue";

    public static void main(String[] args) throws Exception {
        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //消息内容
        String msg = "Hello World";
        //发送消息
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        System.out.println("生产消息:"+msg);
        //关闭资源
        channel.close();
        connection.close();

    }
}
