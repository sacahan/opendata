package com.udn.ntpc.od.core;

import com.udn.ntpc.od.common.CommonApplication;
import com.udn.ntpc.od.model.ModelApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:scheduler.properties", "classpath:gis.properties"}, ignoreResourceNotFound = true)
@ImportResource({"classpath:spring-data.xml"})
@Import({CommonApplication.class, ModelApplication.class})
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

}
