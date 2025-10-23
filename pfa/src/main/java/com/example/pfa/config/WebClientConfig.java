package com.example.pfa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient visionWebClient(@Value("${vision.service.url}") String baseUrl) {
        // augmente le buffer si besoin (retours JSON un peu gros)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(4 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .build();
    }
}
