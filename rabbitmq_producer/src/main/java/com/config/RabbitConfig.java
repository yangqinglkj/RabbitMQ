package com.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



/**
 * @author yangqing
 * @since 2020/5/22  16:54
 *
 *  *
 *  * 基于代码的绑定交换器、队列、路由键设置
 *  * 1. 声明Exchange(交换器名称,durable,autoDelete)
 *  * 2. 声明Queue(队列名称,durable,autoDelete)
 *  * 3. 绑定：BindingBuilder绑定队列到交换器，并设置路由键
 **/
//@Component
@Configuration
public class RabbitConfig {

    /**
     * 创建队列
     * new Queue(String,boolean,boolean,boolean)
     * 1. 队列名称
     * 2. durable 是否持久化 默认true
     * 3. exclusive 排他队列，第一个链接消费后自动删除 默认 false
     * 4. autoDelete 是否自动删除 默认false
     */
    @Bean
    public Queue directQueue(){
        return new Queue("direct.queue");
    }

    /**
     * 创建1:1 类型交换器(direct)
     * new DirectExchange(String,boolean,boolean)
     * new FanoutExchange(String,boolean,boolean)
     * new TopicExchange(String,boolean,boolean)
     * 1. 交换器名称
     * 2. durable 是否持久化 默认true
     * 3. autoDelete 是否自动删除 默认false
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    /**
     * 绑定队列、交换器、路由键
     */
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("HelloWorld");
    }
}
