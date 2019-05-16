package com.udn.ntpc.od.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication // @SpringBootConfiguration, @EnableAutoConfiguration, @ComponentScan
@EnableJpaAuditing // https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#jpa.auditing
//@ServletComponentScan // Ref.: https://www.jianshu.com/p/05c8be17c80a
public class FrontendApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FrontendApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }

}
