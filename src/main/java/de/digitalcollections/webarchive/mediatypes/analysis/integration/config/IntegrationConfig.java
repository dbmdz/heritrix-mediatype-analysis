package de.digitalcollections.webarchive.mediatypes.analysis.integration.config;


import de.digitalcollections.webarchive.mediatypes.analysis.integration.handler.MediaTypeCountingHandler;
import de.digitalcollections.webarchive.mediatypes.analysis.integration.messagesource.CrawlLogMessageSource;
import de.digitalcollections.webarchive.mediatypes.analysis.integration.transformer.FileToLineTransformer;
import de.digitalcollections.webarchive.mediatypes.analysis.integration.transformer.StringToCrawlLogLineTransformer;
import de.digitalcollections.webarchive.mediatypes.analysis.service.TearDownService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class IntegrationConfig {

  @Autowired
  private MediaTypeCountingHandler mediaTypeCountingHandler;

  @Autowired
  private TearDownService tearDownService;

  @Bean
  public CrawlLogMessageSource crawlLogMessageSource(@Value("${inbound.path}") String inboundPath) throws IOException {
    CrawlLogMessageSource messageSource = new CrawlLogMessageSource(inboundPath);
    // messageSource.addObserver((o, message) -> tearDownService.tearDown(message));
    messageSource.addObserver((o, message) -> tearDownService.tearDown());
    return messageSource;
  }

  @Bean
  public IntegrationFlow fileReadingFlow(CrawlLogMessageSource crawlLogMessageSource) throws IOException {
    return IntegrationFlows
        .from(crawlLogMessageSource, e -> e.poller(Pollers.fixedRate(100)))
        .channel(c -> c.executor("fileProcessingChannel", threadPoolExecutor()))
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
  public IntegrationFlow extractMediaTypes() {
    return IntegrationFlows.from("fileProcessingChannel")
        .transform(new FileToLineTransformer())
        .split()
        .transform(new StringToCrawlLogLineTransformer())
        .channel("aggregationChannel")
        .get();
  }

  @Bean
  public IntegrationFlow countMediaTypes() {
    return IntegrationFlows.from("aggregationChannel")
        .handle(mediaTypeCountingHandler)
        .get();

  }

}
