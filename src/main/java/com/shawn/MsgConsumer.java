package com.shawn;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by shawn on 2018/2/1.
 */
@Component
public class MsgConsumer {

    @KafkaListener(topics = {"topic01","topic02"})
    public void processMessage(String content) {
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$" + content);
    }
}
