package com.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangqing
 * @since 2020/5/19  13:51
 *  消费者单纯的使用，其实可以不用添加这个配置，直接建后面的监听就好，
 *  使用注解来让监听器监听对应的队列即可。
 *  配置上了的话，其实消费者也是生成者的身份，也能推送该消息
 **/
@Configuration
public class FanoutRabbitConfig {

    /**
     * 声明三个队列
     * @return
     */
    @Bean
    public Queue queueA() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue queueB() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue queueC() {
        return new Queue("fanout.C");
    }

    /**
     * 声明交换机
     * @return
     */
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    /**
     * 将队列绑定到交换机上
     * @return
     */
    @Bean
    Binding bindingExchangeA() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }

    @Bean
    Binding bindingExchangeB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

    @Bean
    Binding bindingExchangeC() {
        return BindingBuilder.bind(queueC()).to(fanoutExchange());
    }

}
