package com.iccs.graduatoriaservice;

import com.iccs.graduatoriaservice.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class GraduatoriaServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(GraduatoriaServiceApplication.class, args);
    }

}
