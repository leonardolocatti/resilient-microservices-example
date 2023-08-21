package br.com.example;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public final class Client {
  private static final String SERVICE_B_BASE_URL = "http://localhost:8002";
  private static final String SERVICE_C_BASE_URL = "http://localhost:8003";

  private final RestTemplate restTemplate;

  public Client(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getGreetingsFromC() {
    final GreetingsCResponse response =
        this.restTemplate.getForObject(
            SERVICE_C_BASE_URL + "/greetings/c", GreetingsCResponse.class);

    if (response == null) {
      throw new RuntimeException("Invalid response from Service C");
    }

    return response.c;
  }

  public String getGreetingsFromB() {
    final GreetingsBResponse response =
        this.restTemplate.getForObject(
            SERVICE_B_BASE_URL + "/greetings/b", GreetingsBResponse.class);

    if (response == null) {
      throw new RuntimeException("Invalid response from Service B");
    }

    return response.b;
  }

  public GreetingsBCResponse getGreetingsFromBC() {
    final GreetingsBCResponse response =
        this.restTemplate.getForObject(
            SERVICE_B_BASE_URL + "/greetings/bc", GreetingsBCResponse.class);

    return response;
  }

  public record GreetingsBResponse(String b) {}

  public record GreetingsCResponse(String c) {}

  public record GreetingsBCResponse(String b, String c) {}
}
