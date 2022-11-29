package it.antonio.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpecializzazionePersonaleApplication extends SpringBootServletInitializer {

  public static void main (String[] args) {
      SpringApplication.run(SpecializzazionePersonaleApplication.class, args);
  }
}