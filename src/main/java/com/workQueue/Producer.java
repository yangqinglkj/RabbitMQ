package com.workQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.utils.ConnectionUtil;


/**
 * @author yangqing
 * @since 2020/5/12  16:27
 * 生产者
 **/
public class Producer {

    private static final String QUEUE_NAME = "test_work_queue";

    /**
     * 工作队列（work queue）
     * 消费者1和消费者2处理的消息数量是一样的
     * 消费者1 奇数
     * 消费者2 偶数
     * 这种方式是轮训分发，结果是不管谁忙谁清闲，消息的数量是平均分发的
     */
    public static void main(String[] args) throws Exception {
        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        //同一时刻服务器只会发一条消息给消费者,需要消费者手动确认，确认后才会发第二条消息
//        channel.basicQos(1);

        //消息内容
        String msg = "Hello World";
        //发送消息
        for (int i = 1; i <=50; i++) {
            String message = msg+"："+ i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("work queue生产消息:" + message);
            Thread.sleep(i * 10);
        }
        //关闭资源
        channel.close();
        connection.close();

    }
}
