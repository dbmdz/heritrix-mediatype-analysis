package org.mdz.lza.webarchiving.clc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.mdz.lza.integration.transformer.LineTransformer;
import org.mdz.lza.webarchiving.analysis.crawllog.messagesource.CrawlLogMessageSource;
import org.mdz.lza.webarchiving.analysis.crawllog.model.CrawlLogLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableScheduling
@SpringBootApplication
public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private final MimeCounter counter = new MimeCounter();

  private static ApplicationContext context;

  @Value("${inbound.path}")
  private String inboundPath;

  public static void main(String[] args) {
    context = new SpringApplicationBuilder(Application.class)
        .web(false)
        .run(args);
  }

  @Scheduled(fixedRate = 10000L)
  public void memoryInfo() {
    long max = Runtime.getRuntime().maxMemory();
    long maxMB = max / 1024 / 1204;
    logger.info("Max Memory: " + maxMB + " MB (" + max + " bytes)");
    long total = Runtime.getRuntime().totalMemory();
    long totalMB = total / 1024 / 1204;
    logger.info("Total Memory: " + totalMB + " MB (" + total + " bytes)");
  }

  @Bean
  public CrawlLogMessageSource crawlLogMessageSource() throws IOException {
    CrawlLogMessageSource messageSource = new CrawlLogMessageSource(inboundPath);
    messageSource.addObserver((o, message) -> shutdown(message));
    return messageSource;
  }

  @Bean
  public IntegrationFlow fileReadingFlow() throws IOException {
    return IntegrationFlows
        .from(crawlLogMessageSource(), e -> e.poller(Pollers.fixedRate(100)))
        .channel(c -> c.executor("fileProcessingChannel", threadPoolExecutor()))
        // .channel("fileProcessingChannel")
        .get();
  }

  @Bean
  public ThreadPoolTaskExecutor threadPoolExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(6);
    executor.setQueueCapacity(500);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.initialize();
    return executor;
  }

  @Bean
  public IntegrationFlow extractMIME() {
    return IntegrationFlows.from("fileProcessingChannel")
        .transform(new LineTransformer())
        .split()
        .transform(new MimeTypeTransformer())
        .channel("aggregationChannel")
        .get();
  }

  @Bean
  public IntegrationFlow countMime() {
    return IntegrationFlows.from("aggregationChannel")
        .handle(message -> counter.count((CrawlLogLine) message.getPayload()))
        .get();

  }

  @Bean
  public IntegrationFlow resultFlow() {
    return IntegrationFlows.from("outputChannel")
        .handle(System.out::println)
        .get();
  }

  private void writeFile() {
    try (BufferedWriter buffer = new BufferedWriter(new FileWriter("mime.csv"))) {
      Map<String, MimeInfo> mimeInfos = counter.getCounts();
      for (String key : mimeInfos.keySet()) {
        MimeInfo mimeInfo = mimeInfos.get(key);
        buffer.write(key);
        buffer.write(";");
        buffer.write(Integer.toString(mimeInfo.getCount()));
        buffer.write(";");
        buffer.write(Long.toString(mimeInfo.getSize()));
        buffer.newLine();
      }
    } catch (IOException exception) {
      logger.error("Could not write to file", exception);
    }
  }

  @Scheduled(fixedRate = 1000L)
  public void printQueue() {
    logger.info("Queue size: " + threadPoolExecutor().getThreadPoolExecutor().getQueue().size() +
        " Active threads: " + threadPoolExecutor().getActiveCount());
  }

  private void shutdown(Object message) {
    threadPoolExecutor().shutdown();
    logger.info("Processed everything, writing data...");
    writeFile();
    logger.info("Shutting down...");
    SpringApplication.exit(context);
  }
}
