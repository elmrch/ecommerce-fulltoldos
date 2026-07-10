package com.ecommerce.fulltoldos;

import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.NosotrosBloqueService;
import com.ecommerce.fulltoldos.service.PreguntaFrecuenteService;
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
    CommandLineRunner initConfig(ConfiguracionService configService,
                                  NosotrosBloqueService nosotrosBloqueService,
                                  PreguntaFrecuenteService preguntaFrecuenteService) {
        return args -> {
            configService.initDefaults();
            nosotrosBloqueService.seedDefaults();
            preguntaFrecuenteService.seedDefaults();
        };
    }
}
