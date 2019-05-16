package com.udn.ntpc.od.common;

import com.udn.ntpc.od.common.message.util.MessageUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

    @Bean
    public MessageUtil messages() {
        MessageUtil messageUtil = new MessageUtil();
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:message");
        resource.setCacheSeconds(3600); // 30 mins reload
        messageUtil.setResource(resource);
        return messageUtil;
    }

}
