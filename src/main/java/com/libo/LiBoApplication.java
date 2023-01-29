package com.libo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.libo.mapper")
//@EnableDiscoveryClient  // 加载Nacos配置中心配置项
public class LiBoApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.libo.LiBoApplication.class, args);
    }

}
