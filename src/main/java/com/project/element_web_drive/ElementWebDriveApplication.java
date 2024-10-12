package com.project.element_web_drive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.project.element_web_drive"})
@EnableTransactionManagement // 事务
@EnableScheduling // 定时任务
@EnableAsync // 异步调用
public class ElementWebDriveApplication {

    public static void main(String[] args) {

        SpringApplication.run(ElementWebDriveApplication.class, args);
    }

}
