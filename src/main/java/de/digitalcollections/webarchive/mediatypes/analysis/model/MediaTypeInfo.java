package de.digitalcollections.webarchive.mediatypes.analysis.model;


public class MediaTypeInfo {

  private int count;
  private long size;

  public MediaTypeInfo() {
    count = 0;
    size = 0;
  }

  public MediaTypeInfo(int count, long size) {
    this.count = count;
    this.size = size;
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

