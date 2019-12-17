package com.shirly.transaction.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionDispatchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionDispatchServiceApplication.class, args);
        System.out.println("#分配外小哥的运单系统启动完毕，可以通过web访问我啦");
    }

}
