package br.com.example;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configurations {
  private static final int TIMEOUT_MS = 3000;

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.setReadTimeout(Duration.ofMillis(TIMEOUT_MS)).build();
  }
}
