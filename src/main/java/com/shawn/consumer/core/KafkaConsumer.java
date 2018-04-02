package com.shawn.consumer.core;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;


@Component
public class KafkaConsumer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String KAFKA_GROUP = "test01";

    @PostConstruct
    public void init() {
        logger.debug("=====>>>>>>init>>>>>>>>>>>>>>>>>>");
    }


    private static ConsumerFactory<String, String> consumerFactory;

    @Autowired
    public void setConsumerFactory(ConsumerFactory<String, String> consumerFactory) {
        KafkaConsumer.consumerFactory = consumerFactory;
    }

    private static Map<String, Object> consumerConfigs;

    @Autowired
    public void setConsumerConfigs(Map<String, Object> consumerConfigs) {
        KafkaConsumer.consumerConfigs = consumerConfigs;
    }

    /**
     * 监听kafka消息,如果有消息则消费
     *
     * @param record 消息实体bean
     */
    @KafkaListener(topics = "test01", group = KAFKA_GROUP)
    public void executeSession(ConsumerRecord<String, String> record) {
        logger.info("listen record:{}", record.value());

        logger.debug("execute topic:{}, value:{}, key:{}, offset:{}, partition:{}, checksum:{}," +
                "timestampType:{},", record.topic(), record.value(), record.key(), record.offset
                (), record.partition(), record.checksum(), record.timestampType(), record
                .timestamp());
    }


    public static void createKafkaListener(String topic, String group, MessageListener listener) {
        Map<String, Object> consumerConfig = new HashMap<String, Object>();
        consumerConfig.putAll(consumerConfigs);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        CoreKafkaConsumerFactory<String, String> kafkaConsumerFactory =
                new CoreKafkaConsumerFactory<>(consumerConfig, new StringDeserializer(), new StringDeserializer());
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener(listener);
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer<>(kafkaConsumerFactory, containerProperties);

        container.start();
    }
    public static void createKafkaListener(String topic, MessageListener listener) {
        /*Map<String, Object> consumerConfig = ImmutableMap.of(
                BOOTSTRAP_SERVERS_CONFIG, "brokerAddress",
                GROUP_ID_CONFIG, "groupId"
        );

        DefaultKafkaConsumerFactory<String, String> kafkaConsumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        consumerConfig,
                        new StringDeserializer(),
                        new StringDeserializer());*/

        ContainerProperties containerProperties = new ContainerProperties(topic);
        //containerProperties.setMessageListener((MessageListener<String, String>) record -> consumedMessages.add(record.value()));
        containerProperties.setMessageListener(listener);
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);

        container.start();
    }

}
