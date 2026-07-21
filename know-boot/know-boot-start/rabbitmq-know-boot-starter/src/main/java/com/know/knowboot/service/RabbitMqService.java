package com.know.knowboot.service;
import com.know.knowboot.constant.RabbitMqConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /******************************************************** Direct交换机 ************************************************************************/

    /**
     * 发送普通消息
     * <p>
     * convertAndSend： 消息没有顺序，不管是否消费者是否确认，会一直发送消息；
     * convertSendAndReceive： 按照一定的顺序，只有确定消费者接收到消息，才会发送下一条信息，每条消息之间会有间隔时间；
     *
     * @param msg 消息体
     */
    public <T> void sendBaseMsg(T msg, CorrelationData correlationData) {
        // RabbitMqConstant.DEFAULT_BASE_QUEUE 对应配置文件相应的交换机
        rabbitTemplate.convertAndSend(RabbitMqConstant.DEFAULT_BASE_QUEUE, msg, correlationData);
    }

    /**
     * 消息收发(Routing[路由模型])
     *
     * @param msg 消息体
     */
    public <T> void sendRoutingMsg(String routingKey, T msg, CorrelationData correlationData) {
        // 交换机
        rabbitTemplate.convertAndSend(RabbitMqConstant.PUBLISH_SUBSCRITE_DIRECT_EXCHANGE, routingKey, msg, correlationData);
    }

    /******************************************************** Fanout交换机 ************************************************************************/

    /**
     * 消息收发(Publish/Subscrite[发布订阅模式])
     *
     * @param msg 消息体
     */
    public <T> void sendpublishSubscriteeMsg(T msg, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.PUBLISH_SUBSCRITE_QUEUE, msg, correlationData);
    }

    /**
     * 发送FanoutExchange消息
     *
     * @param msg 消息体
     */
    public <T> void sendFanoutMsg(T msg, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.FANOUT_EXCHANGE, null, msg, correlationData);
    }


    /******************************************************** Topic交换机 ************************************************************************/

    /**
     * 发送TopicExchange消息
     * Topics[通配符模式]
     *
     * @param msg 消息体
     */
    public <T> void sendTopicMsg(String routingKey, T msg, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.FANOUT_EXCHANGE, routingKey, msg, correlationData);
    }

    /******************************************************** Header交换机 ************************************************************************/

    /**
     * 发送HeaderExchange消息
     * Topics[通配符模式]
     *
     * @param messages 消息体
     */
    public <T> void sendHeaderMsg(List<Message> messages) {
        if (CollectionUtils.isNotEmpty(messages)) {
            messages.forEach(t -> {
                rabbitTemplate.send(RabbitMqConstant.HEADER_EXCHANGE, null, t);
            });
        }
    }

}
