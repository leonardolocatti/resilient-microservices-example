package br.com.example;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
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

  @Bean
  public CircuitBreaker circuitBreaker() {
    final CircuitBreakerConfig circuitBreakerConfig =
        CircuitBreakerConfig.custom()
            .slidingWindowSize(25)
            .slowCallDurationThreshold(Duration.ofMillis(2150))
            .slowCallRateThreshold(20)
            .permittedNumberOfCallsInHalfOpenState(5)
            .waitDurationInOpenState(Duration.ofMillis(300))
            .build();

    final CircuitBreakerRegistry circuitBreakerRegistry =
        CircuitBreakerRegistry.of(circuitBreakerConfig);

    return circuitBreakerRegistry.circuitBreaker("circuit-breaker");
  }
}
