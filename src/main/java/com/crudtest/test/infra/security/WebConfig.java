//package com.crudtest.test.infra.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class WebConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // aplica a todos los endpoints
//                        .allowedOrigins("http://localhost:3000") // frontend permitido
//                        .allowedMethods("GET", "POST", "PUT", "DELETE") // métodos HTTP permitidos
//                        .allowedHeaders("*"); // permite todos los encabezados
//            }
//        };
//    }
//}
//
