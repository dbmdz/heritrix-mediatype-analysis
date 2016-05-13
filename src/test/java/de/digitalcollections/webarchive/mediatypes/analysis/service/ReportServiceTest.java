package de.digitalcollections.webarchive.mediatypes.service;

import de.digitalcollections.webarchive.mediatypes.service.ReportService;
import de.digitalcollections.webarchive.mediatypes.model.MediaTypeInfo;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Marcus Bitzl <marcus.bitzl@bsb-muenchen.de>
 */
public class ReportServiceTest {

  private ReportService reportService;

  private StringWriter writer;

  @Before
  public void setUp() {
    reportService = new ReportService();
    writer = new StringWriter();
  }

  @Test
  public void testWriteShouldCreateLinesForAllMediaTypes() throws Exception {
    Map<String, MediaTypeInfo> mediaTypeCounts = new HashMap<>();
    mediaTypeCounts.put("text", new MediaTypeInfo(111, 999L));
    mediaTypeCounts.put("application/javascript", new MediaTypeInfo(333, 444L));
    mediaTypeCounts.put("application/pdf", new MediaTypeInfo(111, 999L));
    reportService.write(writer, mediaTypeCounts);
    String lines = writer.toString();
    assertThat(lines.split("\n"), is(arrayWithSize(3)));
  }

  @Test
  public void writeLineShouldCreateValidCsvLine() throws IOException {
    reportService.writeLine(writer, "application/pdf", 123999, 999065);
    assertThat(writer.toString(), is("application/pdf;123999;999065\n"));
  }

}
