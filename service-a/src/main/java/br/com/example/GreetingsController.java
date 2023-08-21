package br.com.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public final class GreetingsController {
  private static final String GREETINGS = "Greetings from Service A";

  private final Client client;

  public GreetingsController(final Client client) {
    this.client = client;
  }

  @GetMapping("/a")
  public GreetingsResponse greetingsB() {
    return new GreetingsResponse(GREETINGS, null, null);
  }

  @GetMapping("/ab")
  public GreetingsResponse greetingsAB() {
    final String greetingsFromB = this.client.getGreetingsFromB();

    return new GreetingsResponse(GREETINGS, greetingsFromB, null);
  }

  @GetMapping("/ac")
  public GreetingsResponse greetingsAC() {
    final String greetingsFromC = this.client.getGreetingsFromC();

    return new GreetingsResponse(GREETINGS, null, greetingsFromC);
  }

  @GetMapping("/abc")
  public GreetingsResponse greetingsABC() {
    final Client.GreetingsBCResponse greetingsBCResponse = this.client.getGreetingsFromBC();

    return new GreetingsResponse(GREETINGS, greetingsBCResponse.b(), greetingsBCResponse.c());
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record GreetingsResponse(String a, String b, String c) {}
}
