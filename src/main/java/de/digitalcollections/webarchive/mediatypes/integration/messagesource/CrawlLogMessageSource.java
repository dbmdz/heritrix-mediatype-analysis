package de.digitalcollections.webarchive.mediatypes.integration.messagesource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CrawlLogMessageSource extends Observable implements MessageSource<File> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CrawlLogMessageSource.class);

  private Iterator<String> directories;

  private final String basePath;

  public CrawlLogMessageSource(String basePath) throws IOException {
    Assert.notNull(basePath);
    this.basePath = basePath;
    File source = new File(basePath);
    if (!source.exists()) {
      throw new IllegalStateException("Source directory does not exist.");
    }

    directories = Arrays.asList(source.list(new RegexFileFilter("\\d+"))).iterator();
  }

  @Override
  public Message<File> receive() {
    if (directories.hasNext()) {
      String directory = directories.next();
      File next = Paths.get(basePath, directory, "logs/crawl.log").toFile();
      LOGGER.info("Process file " + next);
      return new GenericMessage<>(next);
    }
    setChanged();
    notifyObservers();
    return null;
  }

}
