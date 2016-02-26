package org.mdz.lza.webarchiving.clc;

import org.mdz.lza.webarchiving.analysis.crawllog.model.CrawlLogLine;
import org.mdz.lza.webarchiving.analysis.crawllog.parser.CrawlLogParser;
import org.springframework.integration.transformer.GenericTransformer;

/**
 * Created by MBITZL on 12.11.2015.
 */
public class MimeTypeTransformer implements GenericTransformer<String, CrawlLogLine> {

    private CrawlLogParser parser;

    public MimeTypeTransformer() {
        this.parser = new CrawlLogParser();
    }

    @Override
    public CrawlLogLine transform(String line) {
        return parser.parseLine(line);
    }
}
