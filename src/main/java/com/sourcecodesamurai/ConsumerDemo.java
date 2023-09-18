package com.sourcecodesamurai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "pkc-e8mp5.eu-west-1.aws.confluent.cloud:9092");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule   required username=\"GSXOH3AQK3PYTUML\"  " +
                        " password=\"a35AnGs+6bgYJMHxqoIOPBMRx4TqlukNDNm9NtXqMSYcMEwCqVjeolwfvoO8SwlG\";");
        props.put("ssl.endpoint.identification.algorithm", "https");
        props.put("sasl.mechanism", "PLAIN");

        props.put("group.id", "test-consumer-group");
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props,
        new StringDeserializer(),
        new StringDeserializer());

        consumer.subscribe(Arrays.asList("page-visits"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                processRecords(records);

                consumer.commitAsync();
            }
        } catch (Exception e) {
            consumer.close();
        }
}

    private static void processRecords(ConsumerRecords<String, String> records) throws IOException {
        for (ConsumerRecord<String, String> record : records) {

            System.out.printf("Partition = %s, offset=%d, key=%s\n",
                record.partition(),
                record.offset(),
                record.key());
            PageView pageView = toPageView(record.value());
            System.out.println(pageView);
        }
    }

    private static PageView toPageView(String pageViewStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(pageViewStr, PageView.class);
    }
}
