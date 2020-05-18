package com.utils;


import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangqing
 * @since 2020/5/12  16:18
 **/
public class ConnectionUtil {

    /**
     * 获取MQ连接
     *
     * @return Connection连接
     */
    public static Connection getConnection() {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("127.0.01");
        //设置端口号（AMQP协议端口）
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setUsername("user");
        factory.setPassword("root");
        factory.setVirtualHost("/virtual");
        Connection connection = null;
        //通过工厂获取连接
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
