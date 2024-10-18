package com.project.element_web_drive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.project.element_web_drive"})
@EnableTransactionManagement // 事务
@EnableScheduling // 定时任务
@EnableAsync // 异步调用
@MapperScan(basePackages = {"com.project.element_web_drive.mappers"})
public class ElementWebDriveApplication {

    public static void main(String[] args) {

        SpringApplication.run(ElementWebDriveApplication.class, args);
    }

}
