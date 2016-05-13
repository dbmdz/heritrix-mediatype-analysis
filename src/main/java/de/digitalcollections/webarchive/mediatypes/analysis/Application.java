package de.digitalcollections.webarchive.mediatypes.analysis;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableIntegration
public class Application {

  public static void main(String[] args) {
    new SpringApplicationBuilder(Application.class)
        .web(false)
        .run(args);
  }

}
