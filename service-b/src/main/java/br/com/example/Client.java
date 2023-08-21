package br.com.example;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public final class Client {
  private static final String SERVICE_C_BASE_URL = "http://localhost:8003";

  private final CircuitBreaker circuitBreaker;
  private final RestTemplate restTemplate;

  public Client(final CircuitBreaker circuitBreaker, final RestTemplate restTemplate) {
    this.circuitBreaker = circuitBreaker;
    this.restTemplate = restTemplate;
  }

  public String getGreetingsFromC() {
    final GreetingsCResponse response =
        this.circuitBreaker.executeSupplier(
            () ->
                this.restTemplate.getForObject(
                    SERVICE_C_BASE_URL + "/greetings/c", GreetingsCResponse.class));

    return response.c;
  }

  public record GreetingsCResponse(String c) {}
}
