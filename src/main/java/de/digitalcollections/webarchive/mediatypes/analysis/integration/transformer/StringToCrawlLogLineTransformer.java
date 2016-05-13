package de.digitalcollections.webarchive.mediatypes.analysis.integration.transformer;

import de.digitalcollections.webarchive.mediatypes.analysis.model.CrawlLogLine;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class StringToCrawlLogLineTransformer implements GenericTransformer<String, CrawlLogLine> {

  private static final Logger LOGGER = LoggerFactory.getLogger(StringToCrawlLogLineTransformer.class);

  private static final Pattern NUMBER = Pattern.compile("\\d+");

  private static final int MIME_TYPE = 6;
  private static final int RESPONSE_CODE = 1;
  private static final int SIZE = 2;
  private static final int TIMESTAMP = 0;
  private static final int URL = 3;

  public StringToCrawlLogLineTransformer() {

  }

  @Override
  public CrawlLogLine transform(String line) {
    String[] parts = line.split("\\s+");
    if (parts.length < 7) {
      LOGGER.error("Cannot parse this line, skipping: " + line);
      return new CrawlLogLine(0, null, 0, null, null);
    }
    return new CrawlLogLine(
        Integer.parseInt(parts[RESPONSE_CODE]),
        sanitize(parts[MIME_TYPE]),
        parseSize(parts[SIZE]),
        parts[URL],
        DateTime.parse(parts[TIMESTAMP]));
  }

  private long parseSize(String value) {
    if (NUMBER.matcher(value).matches()) {
      return Long.parseLong(value);
    }
    return 0;
  }

  protected String sanitize(String mimeType) {
    String result = mimeType;
    if (result.startsWith("\"")) {
      result = result.substring(1);
    }
    if (result.endsWith("\"")) {
      result = result.substring(0, result.length() - 1);
    }
    return result.toLowerCase();
  }
}
