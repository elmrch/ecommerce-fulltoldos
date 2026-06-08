package com.ecommerce.fulltoldos;

import com.ecommerce.fulltoldos.service.UsuarioService;
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
    public CommandLineRunner crearAdmin(UsuarioService svc) {
        return args -> {
            try {
                    svc.registrarAdmin("Admin", "admin@fulltoldos.com", "admin123");
                System.out.println("✅ Admin creado");
            } catch (Exception e) {
                System.out.println("ℹ️ Admin ya existe");
            }
        };
    }

}
