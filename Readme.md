# Crawl Log Crawler

Parse a large number of Heritrix crawl.log files for line-based processing.
Actually it extracts MIME-Type information, but is extensible. The software
starts at a common parent directory and discoveers crawl.log files recursively.

Usage:

    java -jar crawl-log-crawler.jar --inbound.path=/path/to/harvests
