package com.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yangqing
 * @since 2020/5/19  14:06
 **/
@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {

    @RabbitHandler
    public void HandlerMsg(Map testMessage){
        System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    }
}
