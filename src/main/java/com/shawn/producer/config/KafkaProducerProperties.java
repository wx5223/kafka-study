package com.shawn.producer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * config properties
 * @author Shawn
 */
@ConfigurationProperties(prefix = KafkaProducerProperties.KAFKA_PRODUCER_PREFIX)
public class KafkaProducerProperties {

    public static final String KAFKA_PRODUCER_PREFIX = "kafka";

    private String brokerAddress;

    public String getBrokerAddress() {
        return brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    @Override
    public String toString() {
        return "KafkaProducerProperties{" +
                "brokerAddress='" + brokerAddress + '\'' +
                '}';
    }
}
