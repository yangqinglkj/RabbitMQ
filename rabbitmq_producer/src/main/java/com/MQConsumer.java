package com;

import com.entity.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 基于注解的绑定交换器、队列、路由键设置
 * 1. Queue配置：value=队列名称、durable=是否持久化(默认true)、exclusive=排他队列只在当前connection可用(默认false)、autoDelete=如无消息是否自动删除(默认false)
 * 2. Exchange配置：value=交换器名称、type=类型(默认direct)、durable=是否持久化(默认true)、autoDelete=如无消息是否自动删除(默认false)
 * 3. QueueBinding配置：key=路由键(string数组，支持* # 匹配)，*必须匹配一个单词，#匹配0个或N个单词，用.分隔
 * 4. RabbitListener配置： bindings=Queue配置+Exchange配置+QueueBinding配置
 * 注：如果代码创建交换器等且配置绑定关系，注解只需监听队列即可，如：@RabbitListener(queues = "direct.queue")
 *
 *  消费者
 */
@Component
public class MQConsumer {
    /**
     *  direct 1:1 类型 交换器队列 消费
     */
    @RabbitListener(queues = "direct.queue")
    public void getDirectMessage(User user) throws Exception {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getDirectMessage：" + user.toString());
    }

    /**
     *  配合楼上的队列，消费同一个队列，均匀分配到两个消费者
     */
    @RabbitListener(queues = "direct.queue")
    public void getDirectMessageCopy(User user) throws Exception {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getDirectMessageCopy：" + user.toString());
    }


    /**
     * direct 消息确认
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.queue"), exchange = @Exchange(value = "direct.exchange"), key = "HelloWorld")})
    public void getDirectMessage(User user, Channel channel, Message message) throws Exception {
        try {
            // 模拟执行任务
            Thread.sleep(1000);
            // 模拟异常
            String is = null;
            is.toString();
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                System.out.println("消息已重复处理失败,拒绝再次接收" + user.getName());
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("消息即将再次返回队列处理" + user.getName());
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }

    //    /**
//     *  配合楼上的队列，消费同一个队列，均匀分配到两个消费者
//     */
//    @RabbitListener(queues = "direct.queue")
//    public void getDirectMessageCopy(User user) throws Exception {
//        // 模拟执行任务
//        Thread.sleep(1000);
//        System.out.println("--jxb--MQConsumer--getDirectMessageCopy：" + user.toString());
//    }
    @RabbitListener(queues = "direct.queue")
    public void getDirectMessageCopy(User user, Channel channel, Message message) throws IOException {
        try {
            // 模拟执行任务
            Thread.sleep(1000);
            System.out.println("--jxb--MQConsumer--getDirectMessageCopy：" + user.toString());
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                System.out.println("消息已重复处理失败,拒绝再次接收！");
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("消息即将再次返回队列处理！");
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            e.printStackTrace();
        }
    }


    /**
     * fanout 1:n 类型 交换器队列 消费(3个)
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "fanout.queue.01"), exchange = @Exchange(value = "fanout.exchange", type = "fanout"))})
    public void getFanoutMessage01(String message) throws InterruptedException {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getFanoutMessage01：" + "短信通知：您好，" + message + "!感谢您成为FZB会员");
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "fanout.queue.02"), exchange = @Exchange(value = "fanout.exchange", type = "fanout"))})
    public void getFanoutMessage02(String message) throws InterruptedException {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getFanoutMessage02：" + "增加积分：您好，" + message + "!您的当前积分为100");
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "fanout.queue.03"), exchange = @Exchange(value = "fanout.exchange", type = "fanout"))})
    public void getFanoutMessage03(String message) throws InterruptedException {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getFanoutMessage03：" + "通知好友：您好，您的朋友" + message + "已成为FZB会员，赶快一起互动吧");
    }


    /**
     *  topic n:1 类型 交换器队列 消费(普通会员注册提醒)
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue.01"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"*.reg.msg"})})
    public void getTopicMessage01(String message) throws InterruptedException {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getTopicMessage01：" + "短信通知：您好，" + message + "!感谢您成为FZB会员");
    }

    /**
     *  topic n:1 类型 交换器队列 消费(超级会员注册提醒)
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue.02"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"*.*.reg.msg.#"})})
    public void getTopicMessage02(String message) throws InterruptedException {
        // 模拟执行任务
        Thread.sleep(1000);
        System.out.println("--jxb--MQConsumer--getTopicMessage02：" + "短信通知：您好，" + message + "!感谢您成为FZB超级会员，祝您玩的开心");
    }
}
