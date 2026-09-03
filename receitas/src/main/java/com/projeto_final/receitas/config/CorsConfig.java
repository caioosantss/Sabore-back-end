package com.projeto_final.receitas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Libera o front (Vercel e desenvolvimento local) para consumir a API.
 * Sem isso o navegador bloqueia toda chamada vinda de outra origem.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                // allowedOriginPatterns (e nao allowedOrigins) porque o curinga
                // cobre as URLs de preview, que mudam a cada deploy da Vercel.
                .allowedOriginPatterns(
                        "http://localhost:3000",
                        "https://*.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
