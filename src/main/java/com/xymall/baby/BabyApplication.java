package com.xymall.baby;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.xymall.baby.dao")
@SpringBootApplication()
public class BabyApplication {

    public static void main(String[] args) {
        System.out.println("hello baby");
        SpringApplication.run(BabyApplication.class, args);
    }

}
