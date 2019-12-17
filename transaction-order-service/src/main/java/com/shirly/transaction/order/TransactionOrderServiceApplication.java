package com.shirly.transaction.order;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransactionOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionOrderServiceApplication.class, args);
    }

    // 在rabbit中创建Queue
    @Bean
    public Queue createRabbitmqQueue() {
        return new Queue("orderQueue"); // spring自动在rabbitmq服务器创建
    }
}
