package com.shawn.producer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/")
public class KafkaController {
    public static Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    KafkaProducer kafkaProducer;

    @RequestMapping("/kafka/{topic}")
    @ResponseBody
    public String kafkaProduce(@PathVariable("topic") String topic, String msg) {
        logger.debug("kafkaProduce---> :{}", kafkaProducer);
        logger.debug("getDemo topic:{}, msg:{}", topic, msg);
        kafkaProducer.send(topic, msg);
        return "success";
    }
    //http://localhost:9000/admin/kafka/test01?msg=abcd
}
