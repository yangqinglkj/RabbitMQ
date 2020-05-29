package com;

import com.entity.User;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author yangqing
 * @since 2020/5/22  17:05
 **/
@RestController
public class MQProducer {
    @Autowired
    public RabbitTemplate rabbitTemplate;

    private List<User> users = Arrays.asList(
            new User(1,"张三",18),
            new User(2,"李四",17),
            new User(3,"王五",3)
    );

    /**
     *  direct 1:n 类型 交换器队列 生产
     */
    @GetMapping("/directMQ")
    public List<User> directMQ() {
        for (User user : users) {
            CorrelationData correlationData = new CorrelationData(String.valueOf(user.getId()));
            rabbitTemplate.convertAndSend("direct.exchange", "HelloWorld", user, correlationData);
        }
        return users;
    }

    /**
     *  fanout 1:n 类型 交换器队列 生产
     */
    @GetMapping("/fanoutMQ")
    public List<User> fanoutMQ() {
        for (User user : users) {
            rabbitTemplate.convertAndSend("fanout.exchange", "", user.getName());
        }
        return users;
    }


    /**
     * topic n:1 类型 交换器队列 生产(3个)
     */
    @RequestMapping(value = "/topicMQ01", method = {RequestMethod.GET})
    public List<User> topicMQ01() {
        for (User user : users) {
            rabbitTemplate.convertAndSend("topic.exchange", "jd.reg.msg", user.getName());
        }
        return users;
    }

    @RequestMapping(value = "/topicMQ02", method = {RequestMethod.GET})
    public List<User> topicMQ02() {
        for (User user : users) {
            rabbitTemplate.convertAndSend("topic.exchange", "tm.reg.msg", user.getName());
        }
        return users;
    }

    @RequestMapping(value = "/topicMQ03", method = {RequestMethod.GET})
    public List<User> topicMQ03() {
        for (User user : users) {
            rabbitTemplate.convertAndSend("topic.exchange", "super.fzb.reg.msg", user.getName());
        }
        return users;
    }
}
