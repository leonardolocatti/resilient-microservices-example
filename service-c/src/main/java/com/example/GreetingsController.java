package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsController {
  private static final String GREETINGS = "Greetings from service C";

  @GetMapping("/c")
  public String greetingsFromC() {
    return GREETINGS;
  }
}
