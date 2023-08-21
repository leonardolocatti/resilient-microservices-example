package br.com.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public final class GreetingsController {
  private static final String GREETINGS = "Greetings from Service B";

  private final Client client;

  public GreetingsController(final Client client) {
    this.client = client;
  }

  @GetMapping("/b")
  public GreetingsResponse greetingsB() {
    return new GreetingsResponse(GREETINGS, null);
  }

  @GetMapping("/bc")
  public GreetingsResponse greetingsBC() {
    final String greetingsFromC = this.client.getGreetingsFromC();

    return new GreetingsResponse(GREETINGS, greetingsFromC);
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record GreetingsResponse(String b, String c) {}
}
