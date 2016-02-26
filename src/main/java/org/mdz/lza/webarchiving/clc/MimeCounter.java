package org.mdz.lza.webarchiving.clc;


import org.mdz.lza.webarchiving.analysis.crawllog.model.CrawlLogLine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by MBITZL on 12.11.2015.
 */
public class MimeCounter {

    ConcurrentHashMap<String, MimeInfo> mimeTypes;

    public MimeCounter() {
        mimeTypes = new ConcurrentHashMap<>();
    }

    public void count(CrawlLogLine crawlLogLine) {
        if (!crawlLogLine.hasMimeType()|| crawlLogLine.getHttpCode() != 200) {
            return;
        }
        mimeTypes.compute(crawlLogLine.getMimeType(), (s, info) -> {
            if (info == null) {
                info = new MimeInfo();
            }
            info.addCount(1);
            info.addSize(crawlLogLine.getSize());
            return info;
        });
    }

    public Map<String, MimeInfo> getCounts() {
        return mimeTypes;
    }
}
