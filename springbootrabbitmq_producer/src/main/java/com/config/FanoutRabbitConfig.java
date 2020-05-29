package com.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangqing
 * @since 2020/5/19  14:02
 **/
@Configuration
public class FanoutRabbitConfig {
    /**
     *  创建三个队列 ：fanout.A   fanout.B  fanout.C
     *  将三个队列都绑定在交换机 fanoutExchange 上
     *  因为是扇型交换机, 路由键无需配置,配置也不起作用
     */
    @Bean
    public Queue queueA(){
        return new Queue("fanout.A");
    }
    @Bean
    public Queue queueB() {
        return new Queue("fanout.B");
    }

//    @Bean
//    public Queue queueC() {
//        return new Queue("fanout.C");
//    }

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange");
    }

    /**
     * 将队列绑定到交换机上
     * @return
     */
    @Bean
    public Binding bindingExchangeA(){
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }
    @Bean
    public Binding bindingExchangeB(){
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }
//    @Bean
//    public Binding bindingExchangeC(){
//        return BindingBuilder.bind(queueC()).to(fanoutExchange());
//    }


}
