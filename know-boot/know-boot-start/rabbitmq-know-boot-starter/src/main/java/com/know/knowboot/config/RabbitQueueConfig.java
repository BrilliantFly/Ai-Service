package com.know.knowboot.config;

import com.know.knowboot.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目启动，rabbitmq（队列、交换机）初始化到rabbitmq，推送信息，直接使用交换机名称
 */
@Configuration
public class RabbitQueueConfig {

    /**
     * 消息收发(基本消息模型)
     * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
     * exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
     * autoDelete:是否自动删除，有消息者订阅本队列，然后所有消费者都解除订阅此队列，会自动删除。
     * arguments：队列携带的参数，比如设置队列的死信队列，消息的过期时间等等。
     *
     * @return
     */
    @Bean
    Queue baseQueue() {
        return new Queue(RabbitMqConstant.DEFAULT_BASE_QUEUE, true, false, true);
    }

    /**
     * 消息收发(Publish/Subscrite[发布订阅模式])
     * DirectExchange 的路由策略是将消息队列绑定到一个 DirectExchange 上，
     * 当一条消息到达 DirectExchange 时会被转发到与该条消息 routing key 相同的 Queue 上，
     * 例如消息队列名为 “hello-queue”，则 routingkey 为 “hello-queue” 的消息会被该消息队列接收
     *
     * @return
     */
    @Bean
    Queue publishSubscriteQueue() {
        return new Queue(RabbitMqConstant.PUBLISH_SUBSCRITE_QUEUE, true, false, true);
    }

    /**
     * durable:是否持久化,默认是false,持久化交换机。
     * autoDelete:是否自动删除，交换机先有队列或者其他交换机绑定的时候，然后当该交换机没有队列或其他交换机绑定的时候，会自动删除。
     * arguments：交换机设置的参数，比如设置交换机的备用交换机（Alternate Exchange），当消息不能被路由到该交换机绑定的队列上时，会自动路由到备用交换机
     *
     * @return
     */
    @Bean
    DirectExchange publishSubscriteDirectExchange() {
        return new DirectExchange(RabbitMqConstant.PUBLISH_SUBSCRITE_DIRECT_EXCHANGE, true, false);
    }

    /**
     * bind队列to交换机中with路由key（routing key）
     * <p>
     * 消息收发(Routing[路由模型])
     *
     * @return
     */
    @Bean
    Binding publishSubscriteBinding() {
        return BindingBuilder.bind(publishSubscriteQueue()).to(publishSubscriteDirectExchange()).with("direct");
    }


    @Bean
    Queue commonQueueOne() {
        return new Queue(RabbitMqConstant.COMMON_QUEUE_ONE, true, false, true);
    }

    @Bean
    Queue commonQueueTWO() {
        return new Queue(RabbitMqConstant.COMMON_QUEUE_TWO, true, false, true);
    }

    @Bean
    Queue commonQueueThree() {
        return new Queue(RabbitMqConstant.COMMON_QUEUE_THREE, true, false, true);
    }

    @Bean
    Queue commonQueueFour() {
        return new Queue(RabbitMqConstant.COMMON_QUEUE_FOUR, true, false, true);
    }

    /**
     * FanoutExchange 的数据交换策略是把所有到达 FanoutExchange 的消息转发给所有与它绑定的 Queue 上，在这种策略中，routingkey 将不起任何作用
     *
     * @return
     */
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitMqConstant.FANOUT_EXCHANGE, true, false);
    }

    @Bean
    Binding fanoutExchangeBindingOne() {
        return BindingBuilder.bind(commonQueueOne()).to(fanoutExchange());
    }

    @Bean
    Binding fanoutExchangeBTwo() {
        return BindingBuilder.bind(commonQueueTWO()).to(fanoutExchange());
    }

    /**
     * TopicExchange 是比较复杂但是也比较灵活的一种路由策略，在 TopicExchange 中，Queue 通过 routingkey 绑定到 TopicExchange 上，
     * 当消息到达 TopicExchange 后，TopicExchange 根据消息的 routingkey 将消息路由到一个或者多个 Queue 上
     *
     * @return
     */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(RabbitMqConstant.TOPIC_EXCHANGE, true, false);
    }

    /**
     * Topics[通配符模式]
     *
     * @return
     */
    @Bean
    Binding topicExchangeBindingOne() {
        return BindingBuilder.bind(commonQueueOne()).to(topicExchange()).with(RabbitMqConstant.COMMON_QUEUE_ONE + ".#");
    }

    /**
     * Topics[通配符模式]
     * *：只能匹配一个单词；
     * #：可以匹配零个或多个单词
     *
     * @return
     */
    @Bean
    Binding topicExchangeBTwo() {
        return BindingBuilder.bind(commonQueueTWO()).to(topicExchange()).with("#." + RabbitMqConstant.COMMON_QUEUE_TWO + ".#");
    }

    /**
     * HeadersExchange 是一种使用较少的路由策略，HeadersExchange 会根据消息的 Header 将消息路由到不同的 Queue 上，这种策略也和 routingkey无关
     */
    @Bean
    HeadersExchange headersExchange() {
        return new HeadersExchange(RabbitMqConstant.HEADER_EXCHANGE, true, false);
    }

    @Bean
    Binding bindingRabbit() {
        Map<String, Object> map = new HashMap<>();
        map.put("rabbit", "rabbit");
        return BindingBuilder.bind(commonQueueThree())
                .to(headersExchange()).whereAny(map).match();
    }

    @Bean
    Binding bindingMq() {
        return BindingBuilder.bind(commonQueueFour())
                .to(headersExchange()).where("mq").exists();
    }

}
