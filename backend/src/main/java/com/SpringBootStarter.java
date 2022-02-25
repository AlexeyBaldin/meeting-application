package com;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Meeting application backend API", version = "1.0"))
public class SpringBootStarter {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("SecurityConfig.passwordEncoder");
        return bCryptPasswordEncoder;
    }
}
