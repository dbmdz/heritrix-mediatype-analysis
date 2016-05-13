package de.digitalcollections.webarchive.mediatypes.analysis.integration.handler;

import de.digitalcollections.webarchive.mediatypes.analysis.model.CrawlLogLine;
import de.digitalcollections.webarchive.mediatypes.analysis.model.MediaTypeInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.stereotype.Component;

@Component
public class MediaTypeCountingHandler implements GenericHandler<CrawlLogLine> {

  private ConcurrentHashMap<String, MediaTypeInfo> mimeTypeCounts;

  public MediaTypeCountingHandler() {
    mimeTypeCounts = new ConcurrentHashMap<>();
  }

  @Override
  public Object handle(CrawlLogLine crawlLogLine, Map<String, Object> headers) {
    if (!crawlLogLine.hasMimeType() || crawlLogLine.getHttpCode() != 200) {
      return null;
    }
    mimeTypeCounts.compute(crawlLogLine.getMimeType(), (s, info) -> {
      if (info == null) {
        info = new MediaTypeInfo();
      }
      info.addCount(1);
      info.addSize(crawlLogLine.getSize());
      return info;
    });
    return null;
  }

  public Map<String, MediaTypeInfo> getMimeTypeCounts() {
    return mimeTypeCounts;
  }
}
