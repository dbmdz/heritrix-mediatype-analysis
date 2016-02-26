package org.mdz.lza.webarchiving.clc;

import org.junit.Before;
import org.junit.Test;
import org.mdz.lza.webarchiving.analysis.crawllog.model.CrawlLogLine;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by MBITZL on 12.11.2015.
 */
public class MimeCounterTest {

    private MimeCounter counter;

    @Before
    public void setUp() {
        counter = new MimeCounter();
    }

    @Test
    public void shouldCountMimeType() {
        counter.count(new CrawlLogLine(200, "text/html", 0, null, null));
        counter.count(new CrawlLogLine(200, "text/html", 0, null, null));
        counter.count(new CrawlLogLine(200, "application/javascript", 0, null, null));
        counter.count(new CrawlLogLine(200, "text/html", 0, null, null));
        assertThat(counter.getCounts().get("text/html").getCount(), is(3));
    }

    @Test
    public void shouldCountOnlyHttpCode200() {
        counter.count(new CrawlLogLine(200, "text/html", 0, null, null));
        counter.count(new CrawlLogLine(404, "text/html", 0, null, null));
        counter.count(new CrawlLogLine(200, "text/html", 0, null, null));
        assertThat(counter.getCounts().get("text/html").getCount(), is(2));
    }

    @Test
    public void shouldAddSizeOnlyHttpCode200() {
        counter.count(new CrawlLogLine(200, "text/html", 100, null, null));
        counter.count(new CrawlLogLine(404, "text/html", 200, null, null));
        counter.count(new CrawlLogLine(200, "text/html", 300, null, null));
        assertThat(counter.getCounts().get("text/html").getSize(), is(400L));
    }




}