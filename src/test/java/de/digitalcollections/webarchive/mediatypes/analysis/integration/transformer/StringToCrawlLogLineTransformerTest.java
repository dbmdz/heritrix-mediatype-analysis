package de.digitalcollections.webarchive.mediatypes.analysis.integration.transformer;

import de.digitalcollections.webarchive.mediatypes.analysis.integration.transformer.StringToCrawlLogLineTransformer;
import de.digitalcollections.webarchive.mediatypes.analysis.model.CrawlLogLine;
import static org.hamcrest.Matchers.nullValue;
import org.hamcrest.core.Is;
import static org.hamcrest.core.Is.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Marcus Bitzl <marcus.bitzl@bsb-muenchen.de>
 */
public class StringToCrawlLogLineTransformerTest {

  private StringToCrawlLogLineTransformer transformer;

  @Before
  public void setUp() throws Exception {
    transformer = new StringToCrawlLogLineTransformer();
  }

  @Test
  public void shouldExtractMimeTypeForFoundFile() {
    String source = "2014-03-17T15:22:16.679Z   200       4851 " +
        "http://ajax.googleapis.com/robots.txt EP " +
        "http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/plain #001 " +
        "20140317152216646+32 sha1:HC3C4E7J27T5MKC4XPZDNA6RV76FE2F3 - -";
    assertThat(transformer.transform(source).getMimeType(), is("text/plain"));
  }

  @Test
  public void shouldExtractMimeTypeForFileNotFound() {
    String source = "2014-03-17T15:22:19.612Z   404        213 " +
        "http://www.sadmoon.de/text/javascript " +
        "EX http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/html #002 " +
        "20140317152219586+26 sha1:OS2OPUB7C7MDGVSKUJAFMHKHY22G7NYZ - -";
    assertThat(transformer.transform(source).getMimeType(), is("text/html"));
  }

  @Test
  public void shouldExtractHttpErrorCode() {
    String source = "2014-03-17T15:22:19.612Z   404        213 " +
        "http://www.sadmoon.de/text/javascript EX " +
        "http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/html #002 " +
        "20140317152219586+26 sha1:OS2OPUB7C7MDGVSKUJAFMHKHY22G7NYZ - -";
    assertThat(transformer.transform(source).getHttpCode(), is(404));
  }

  @Test
  public void shouldExtractSize() {
    String source = "2014-03-17T15:22:19.612Z   404        213 " +
        "http://www.sadmoon.de/text/javascript EX " +
        "http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/html #002 " +
        "20140317152219586+26 sha1:OS2OPUB7C7MDGVSKUJAFMHKHY22G7NYZ - -";
    assertThat(transformer.transform(source).getSize(), Is.is(213L));
  }

  @Test
  public void shouldExtractUrl() {
    String source = "2014-03-17T15:22:19.612Z   404        213 " +
        "http://www.sadmoon.de/text/javascript EX " +
        "http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/html #002 " +
        "20140317152219586+26 sha1:OS2OPUB7C7MDGVSKUJAFMHKHY22G7NYZ - -";
    assertThat(transformer.transform(source).getUrl(), is("http://www.sadmoon.de/text/javascript"));
  }

  @Test
  public void shouldExtractTimestamp() {
    String source = "2014-03-17T15:22:19.612Z   404        213 " +
        "http://www.sadmoon.de/text/javascript EX " +
        "http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/html #002 " +
        "20140317152219586+26 sha1:OS2OPUB7C7MDGVSKUJAFMHKHY22G7NYZ - -";
    assertThat(transformer.transform(source).getTimestamp(), is(DateTime.parse("2014-03-17T15:22:19.612Z")));
  }

  @Test
  public void sanitizeShouldRemoveLeadingQuotes() {
    assertThat(transformer.sanitize("\"test"), is("test"));
  }

  @Test
  public void sanitizeShouldRemoveTrailingQuotes() {
    assertThat(transformer.sanitize("test\""), is("test"));
  }

  @Test
  public void sanitizeShouldLeaveGoodDataUntouched() {
    assertThat(transformer.sanitize("test"), is("test"));
  }

  @Test
  public void sanitizeConvertToLowercase() {
    assertThat(transformer.sanitize("Test"), is("test"));
  }

  @Test
  public void badLinesShouldBeSkipped() {
    CrawlLogLine line = transformer.transform("adjakhhsdkashdkjahsdkasjhkd");
    assertThat(line.getMimeType(), is(nullValue()));
  }

}
