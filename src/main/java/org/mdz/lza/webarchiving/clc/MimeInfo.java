package org.mdz.lza.webarchiving.clc;

/**
 * Created by MBITZL on 12.11.2015.
 */
public class MimeInfo {

    private int count;
    private long size;

    public MimeInfo() {
        count = 0;
        size = 0;
    }

    public void addCount(int value) {
        count = count + value;
    }

    public void addSize(long value) {
        size = size + value;
    }

    public int getCount() {
        return count;
    }

    public long getSize() {
        return size;
    }
}
