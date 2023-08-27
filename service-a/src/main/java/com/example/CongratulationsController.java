package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/congratulations")
public class CongratulationsController {
  private static final String CONGRATULATIONS = "Congratulations from service A";

  @Autowired private Client client;

  @GetMapping("/a")
  public String congratulationsFromA() {
    return CONGRATULATIONS;
  }

  @GetMapping("/ab")
  public String congratulationsFromAB() {
    String congratulationsFromB = client.congratulationsFromB();

    return CONGRATULATIONS + " and " + congratulationsFromB;
  }

  @GetMapping("/ac")
  public String congratulationsFromAC() {
    String congratulationsFromC = client.congratulationsFromC();

    return CONGRATULATIONS + " and " + congratulationsFromC;
  }
}
