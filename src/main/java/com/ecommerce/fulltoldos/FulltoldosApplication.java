package com.ecommerce.fulltoldos;

import com.ecommerce.fulltoldos.service.ConfiguracionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FulltoldosApplication {
    public static void main(String[] args) {
        SpringApplication.run(FulltoldosApplication.class, args);
    }

    @Bean
    CommandLineRunner initConfig(ConfiguracionService configService) {
        return args -> configService.initDefaults();
    }
}
