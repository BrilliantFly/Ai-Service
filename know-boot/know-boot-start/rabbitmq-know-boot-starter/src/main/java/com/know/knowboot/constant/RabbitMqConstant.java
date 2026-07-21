package com.know.knowboot.constant;

import lombok.Data;

@Data
public class RabbitMqConstant {

    /**
     * 默认的交换机，提供一个生产者一个队列以及一个消费者"
     */
    public static String DEFAULT_BASE_QUEUE = "default_base_queue";

    /**
     * 消息收发(Publish/Subscrite[发布订阅模式])
     */
    public static String PUBLISH_SUBSCRITE_QUEUE = "public_subscrite_queue";
    public static String PUBLISH_SUBSCRITE_DIRECT_EXCHANGE = "public_subscrite_direct_exchange";

    public static String COMMON_QUEUE_ONE = "common-queue-one";
    public static String COMMON_QUEUE_TWO = "common-queue-two";
    public static String COMMON_QUEUE_THREE = "common-queue-three";
    public static String COMMON_QUEUE_FOUR = "common-queue-four";

    public static String FANOUT_EXCHANGE = "fanout-exchange";

    public static String TOPIC_EXCHANGE = "topic-exchange";

    public static String HEADER_EXCHANGE = "header-exchange";

}
