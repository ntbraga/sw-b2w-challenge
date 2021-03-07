package br.com.b2w.b2wchallenge.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean("swapi")
    public RestTemplate restTemplateIbge() {
        return new RestTemplateBuilder()
                .rootUri("https://swapi.dev/api/")
                .build();
    }

}
