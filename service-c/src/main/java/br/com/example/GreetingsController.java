package br.com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public final class GreetingsController {
  private static final int RESPONSE_DELAY_MS = 1000;
  private static final String GREETINGS = "Greetings from Service C";

  @GetMapping("/c")
  public GreetingsResponse greetingsC() throws InterruptedException {
    Thread.sleep(RESPONSE_DELAY_MS);

    return new GreetingsResponse(GREETINGS);
  }

  public record GreetingsResponse(String c) {}
}
