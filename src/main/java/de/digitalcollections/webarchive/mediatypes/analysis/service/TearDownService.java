package de.digitalcollections.webarchive.mediatypes.analysis.service;

import de.digitalcollections.webarchive.mediatypes.analysis.integration.handler.MediaTypeCountingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class TearDownService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TearDownService.class);

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ReportService reportService;

  @Autowired
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  @Autowired
  private MediaTypeCountingHandler mediaTypeCountingHandler;

  @Value("${out}")
  private String outputFilename;

  public void tearDown() {
    threadPoolTaskExecutor.shutdown();
    LOGGER.info("Processed everything, writing data...");
    reportService.writeFile(outputFilename, mediaTypeCountingHandler.getMimeTypeCounts());
    LOGGER.info("Shutting down...");
    SpringApplication.exit(applicationContext);
  }

}
