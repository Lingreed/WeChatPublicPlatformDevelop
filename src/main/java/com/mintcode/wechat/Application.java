package com.mintcode.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * Author: Jeffrey.pan
 * Date: 2015-10-23
 * Description: 服务主入口
 */
@SpringBootApplication
@ImportResource("classpath:spring-ctx.xml")
public class Application {
	
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Application.class);
        ApplicationContext ctx =app.run(args);
    }
    
}
