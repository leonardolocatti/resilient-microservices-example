package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/congratulations")
public class CongratulationsController {
  private static final long DELAY_RESPONSE_MS = 2800;
  private static final String CONGRATULATIONS = "Congratulations from service B";

  @GetMapping("/b")
  public String congratulationsFromB() throws InterruptedException {
    Thread.sleep(DELAY_RESPONSE_MS);

    return CONGRATULATIONS;
  }
}
