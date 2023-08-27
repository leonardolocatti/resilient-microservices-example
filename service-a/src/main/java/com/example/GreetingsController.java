package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsController {
  private static final String GREETINGS = "Greetings from service A";

  @Autowired private Client client;

  @GetMapping("/a")
  public String greetingsFromA() {
    return GREETINGS;
  }

  @GetMapping("/ab")
  public String greetingsFromAB() {
    String greetingsFromB = client.greetingsFromB();

    return GREETINGS + " and " + greetingsFromB;
  }

  @GetMapping("/ac")
  public String greetingsFromAC() {
    String greetingsFromC = client.greetingsFromC();

    return GREETINGS + " and " + greetingsFromC;
  }
}
