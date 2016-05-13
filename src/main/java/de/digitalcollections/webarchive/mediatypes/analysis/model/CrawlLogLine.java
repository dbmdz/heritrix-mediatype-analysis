package de.digitalcollections.webarchive.mediatypes.model;

import org.joda.time.DateTime;

/**
 * Created by MBITZL on 12.11.2015.
 */
public class CrawlLogLine {

    private final int httpCode;
    private final String mimeType;
    private final long size;

    private final DateTime timestamp;
    private final String url;

    public CrawlLogLine(int httpCode, String mimeType, long size, String url, DateTime timestamp) {
        this.httpCode = httpCode;
        this.mimeType = mimeType;
        this.size = size;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getHttpCode() {
        return httpCode;
    }

    /**
     * @return The file's size in bytes.
     */
    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasMimeType() {
        return mimeType != null;
    }

    @Override
    public String toString() {
        return String.format("CrawlLogLine(%d, %s, %d)", httpCode, mimeType, size);
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
