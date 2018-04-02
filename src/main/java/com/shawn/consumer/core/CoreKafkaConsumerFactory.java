package com.shawn.consumer.core;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shawn on 2018/4/2.
 */
public class CoreKafkaConsumerFactory<K, V> implements ConsumerFactory<K, V> {

    private final Map<String, Object> configs;

    private Deserializer<K> keyDeserializer;

    private Deserializer<V> valueDeserializer;

    public CoreKafkaConsumerFactory(Map<String, Object> configs) {
        this(configs, null, null);
    }

    public CoreKafkaConsumerFactory(Map<String, Object> configs,
                                       Deserializer<K> keyDeserializer,
                                       Deserializer<V> valueDeserializer) {
        this.configs = new HashMap<>(configs);
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
    }

    public void setKeyDeserializer(Deserializer<K> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public void setValueDeserializer(Deserializer<V> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public Consumer<K, V> createConsumer() {
        return createKafkaConsumer();
    }

    protected org.apache.kafka.clients.consumer.KafkaConsumer<K, V> createKafkaConsumer() {
        return new org.apache.kafka.clients.consumer.KafkaConsumer<K, V>(this.configs, this.keyDeserializer, this.valueDeserializer);
    }

    @Override
    public boolean isAutoCommit() {
        Object auto = this.configs.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);
        return auto instanceof Boolean ? (Boolean) auto
                : auto instanceof String ? Boolean.valueOf((String) auto) : false;
    }

    public Map<String, Object> getConfigs() {
        return configs;
    }

}

