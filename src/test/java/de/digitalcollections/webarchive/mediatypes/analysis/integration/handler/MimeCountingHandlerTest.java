package de.digitalcollections.webarchive.mediatypes.analysis.integration.handler;

import de.digitalcollections.webarchive.mediatypes.analysis.integration.handler.MediaTypeCountingHandler;
import de.digitalcollections.webarchive.mediatypes.analysis.model.CrawlLogLine;
import static java.util.Collections.emptyMap;
import java.util.Map;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Marcus Bitzl <marcus.bitzl@bsb-muenchen.de>
 */
public class MimeCountingHandlerTest {

  private Map<String, Object> headers;

  private MediaTypeCountingHandler handler;

  @Before
  public void setUp() {
    handler = new MediaTypeCountingHandler();
    headers = emptyMap();
  }

  @Test
  public void shouldCountMimeType() {
    handler.handle(new CrawlLogLine(200, "text/html", 0, null, null), headers);
    handler.handle(new CrawlLogLine(200, "text/html", 0, null, null), headers);
    handler.handle(new CrawlLogLine(200, "application/javascript", 0, null, null), headers);
    handler.handle(new CrawlLogLine(200, "text/html", 0, null, null), headers);
    assertThat(handler.getMimeTypeCounts().get("text/html").getCount(), is(3));
  }

  @Test
  public void shouldCountOnlyHttpCode200() {
    handler.handle(new CrawlLogLine(200, "text/html", 0, null, null), headers);
    handler.handle(new CrawlLogLine(404, "text/html", 0, null, null), headers);
    handler.handle(new CrawlLogLine(200, "text/html", 0, null, null), headers);
    assertThat(handler.getMimeTypeCounts().get("text/html").getCount(), is(2));
  }

  @Test
  public void shouldAddSizeOnlyHttpCode200() {
    handler.handle(new CrawlLogLine(200, "text/html", 100, null, null), headers);
    handler.handle(new CrawlLogLine(404, "text/html", 200, null, null), headers);
    handler.handle(new CrawlLogLine(200, "text/html", 300, null, null), headers);
    assertThat(handler.getMimeTypeCounts().get("text/html").getSize(), is(400L));
  }

}
