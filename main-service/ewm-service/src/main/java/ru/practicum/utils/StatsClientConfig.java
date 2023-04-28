package ru.practicum.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.Client;

@Configuration
public class StatsClientConfig {

    @Value("${stats-service.url}")
    private String url;

    @Bean
    public Client client() {
        return new Client(url);
    }
}