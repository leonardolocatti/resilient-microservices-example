package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsController {
  private static final long DELAY_RESPONSE_MS = 1000;
  private static final String GREETINGS = "Greetings from service B";

  @GetMapping("/b")
  public String greetingsFromB() throws InterruptedException {
    Thread.sleep(DELAY_RESPONSE_MS);

    return GREETINGS;
  }
}
