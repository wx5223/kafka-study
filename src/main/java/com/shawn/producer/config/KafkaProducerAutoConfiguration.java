package com.shawn.producer.config;


import com.shawn.producer.core.KafkaProducer;
import com.shawn.producer.core.SimplePartitioner;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * config properties
 * @author Shawn
 */
@Configuration
@EnableConfigurationProperties(KafkaProducerProperties.class)
@ConditionalOnClass(value = KafkaTemplate.class)
public class KafkaProducerAutoConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private KafkaProducerProperties kafkaProducerProperties;

    public KafkaProducerAutoConfiguration(KafkaProducerProperties kafkaProducerProperties) {
        logger.debug("KafkaProducerAutoConfiguration kafkaProducerProperties:{}", kafkaProducerProperties);
        this.kafkaProducerProperties = kafkaProducerProperties;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        logger.debug("producerConfigs brokerAddress:{}", kafkaProducerProperties.getBrokerAddress());
        String brokers = kafkaProducerProperties.getBrokerAddress();
        if (StringUtils.isEmpty(brokers)) {
            throw new RuntimeException("kafka broker address is emptiy");
        }
        Map<String, Object> props = new HashMap<String, Object>();
        // list of host:port pairs used for establishing the initial connections
        // to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties
                .getBrokerAddress());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // value to block, after which it will throw a TimeoutException
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SimplePartitioner.class);


        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        return props;
    }


    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaProducer kafkaProducer() {
        logger.debug("kafkaProducer init");
        return new KafkaProducer(kafkaTemplate());
    }

}
