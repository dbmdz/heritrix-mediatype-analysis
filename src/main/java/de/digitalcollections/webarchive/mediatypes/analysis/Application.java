package de.digitalcollections.webarchive.mediatypes;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableIntegration
public class Application {
  //
  // private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
  //
  // @Autowired
  // private static ApplicationContext context;
  //
  // @Autowired
  // private MediaTypeCountingHandler mediaTypeCountingHandler;
  //
  // @Autowired
  // private ReportService reportService;

  public static void main(String[] args) {
    new SpringApplicationBuilder(Application.class)
        .web(false)
        .run(args);
  }

  // @Bean
  // public CrawlLogMessageSource crawlLogMessageSource(@Value("${inbound.path}") String inboundPath) throws IOException {
  // CrawlLogMessageSource messageSource = new CrawlLogMessageSource(inboundPath);
  // messageSource.addObserver((o, message) -> shutdown(message));
  // return messageSource;
  // }
  //
  // @Bean
  // public IntegrationFlow fileReadingFlow(CrawlLogMessageSource crawlLogMessageSource) throws IOException {
  // return IntegrationFlows
  // .from(crawlLogMessageSource, e -> e.poller(Pollers.fixedRate(100)))
  // .channel(c -> c.executor("fileProcessingChannel", threadPoolExecutor()))
  // .get();
  // }
  //
  // @Bean
  // public ThreadPoolTaskExecutor threadPoolExecutor() {
  // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  // executor.setCorePoolSize(5);
  // executor.setMaxPoolSize(6);
  // executor.setQueueCapacity(500);
  // executor.setWaitForTasksToCompleteOnShutdown(true);
  // executor.initialize();
  // return executor;
  // }
  //
  // @Bean
  // public IntegrationFlow extractMediaTypes() {
  // return IntegrationFlows.from("fileProcessingChannel")
  // .transform(new FileToLineTransformer())
  // .split()
  // .transform(new StringToCrawlLogLineTransformer())
  // .channel("aggregationChannel")
  // .get();
  // }
  //
  // @Bean
  // public IntegrationFlow countMediaTypes() {
  // return IntegrationFlows.from("aggregationChannel")
  // .handle(mediaTypeCountingHandler)
  // .get();
  //
  // }
  //
  // private void shutdown(Object message) {
  // threadPoolExecutor().shutdown();
  // LOGGER.info("Processed everything, writing data...");
  // reportService.writeFile("mime.csv", mediaTypeCountingHandler.getMimeTypeCounts());
  // LOGGER.info("Shutting down...");
  // SpringApplication.exit(context);
  // }
}
