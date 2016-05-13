package de.digitalcollections.webarchive.mediatypes.integration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MonitoringConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringConfig.class);

  @Autowired
  private ThreadPoolTaskExecutor executor;

  @Scheduled(fixedRate = 10000L)
  public void memoryInfo() {
    long max = Runtime.getRuntime().maxMemory();
    long maxMB = max / 1024 / 1204;
    LOGGER.info("Max Memory: " + maxMB + " MB (" + max + " bytes)");
    long total = Runtime.getRuntime().totalMemory();
    long totalMB = total / 1024 / 1204;
    LOGGER.info("Total Memory: " + totalMB + " MB (" + total + " bytes)");
  }

  @Scheduled(fixedRate = 1000L)
  public void printQueue() {
    LOGGER.info("Queue size: " + executor.getThreadPoolExecutor().getQueue().size() +
        " Active threads: " + executor.getActiveCount());
  }

}
