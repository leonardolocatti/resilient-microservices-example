package com.example;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Client {
  private static final String SERVICE_B_BASE_URL = "http://localhost:8002/service-b";
  private static final String SERVICE_C_BASE_URL = "http://localhost:8003/service-c";

  @Autowired private RestTemplate restTemplate;

  @CircuitBreaker(name = "service-b", fallbackMethod = "greetingsFromBFallback")
  public String greetingsFromB() {
    return restTemplate.getForObject(SERVICE_B_BASE_URL + "/greetings/b", String.class);
  }

  public String greetingsFromBFallback(Exception e) {
    return congratulationsFromC();
  }

  public String congratulationsFromB() {
    return restTemplate.getForObject(SERVICE_B_BASE_URL + "/congratulations/b", String.class);
  }

  public String greetingsFromC() {
    return restTemplate.getForObject(SERVICE_C_BASE_URL + "/greetings/c", String.class);
  }

  public String congratulationsFromC() {
    return restTemplate.getForObject(SERVICE_C_BASE_URL + "/congratulations/c", String.class);
  }
}
