package com.feifei.ddd.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author xiaofeifei
 */
@SpringBootApplication
@MapperScan(basePackages = "com.feifei.ddd.demo.infrastructure.jpa.mapper")
public class SpringbootDDDDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootDDDDemoApplication.class, args);
    }
}
