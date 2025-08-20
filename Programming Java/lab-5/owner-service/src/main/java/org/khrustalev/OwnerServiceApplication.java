package org.khrustalev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OwnerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnerServiceApplication.class, args);
    }
}
