package com.sourcecodesamurai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class ProducerDemo {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "pkc-rgm37.us-west-2.aws.confluent.cloud:9092");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username={{XAP7FD6EXMHNW5TE}} password={{goTVHH+BLXdzO+3aGd5Ul97WSkU1d9OPj98aWfpvhWM3+e3hn4DL5D4zg8VrOjUD}};");
        props.put("session.timeout.ms", "45000");
        props.put("sslendpoint.identification.algorithm.", "https");
        props.put("sasl.mechanism", "PLAIN");
        props.put("retries", "3");

        Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());

            for(int i = 0; i < 100; i++) {
                PageView pageView = generateRecord();
                String pageViewStr = toJsonString(pageView);
                System.out.println(pageView);
                sleep(500);

                RecordMetadata metadata = producer.send(new ProducerRecord<>("page-visits", pageView.getUserName(), pageViewStr)).get();

                System.out.println(String.format("Key = %s; partition = %s; offset = %s",
                 pageView.getUserName(),
                 metadata.partition(),
                 metadata.offset()));
                System.out.println("--------------------------------------------------");   
            }
        producer.flush();
        producer.close();
    }
        private static String toJsonString(PageView pageView) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(pageView);
        }

        private static PageView generateRecord() {
                Faker faker = new Faker();

                PageView pageView = new PageView();
                pageView.setUserName(faker.name().username());
                pageView.setBroswer(faker.internet().userAgentAny());
                pageView.setPage(randomPage());
                pageView.setViewDate(new Date());

                return pageView;
        }

        private static String randomPage() {
            return ramdomSelect(new String[] {
                "Home", "Search", "Product", "Cart", "Checkout", "/search", "/purchase"
            });
        }

        private static String ramdomSelect(String[] arr) {
            int rnd = new Random().nextInt(arr.length);
            return arr[rnd];
        }

        private static void sleep(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}
