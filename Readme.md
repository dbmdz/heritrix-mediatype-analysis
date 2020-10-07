# Crawl Log Media Type Analysis
[![Build Status](https://travis-ci.org/dbmdz/heritrix-mediatype-analysis.svg?branch=master)](https://travis-ci.org/dbmdz/heritrix-mediatype-analysis)
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE.md)

## Usage

### Analyze Media Types

Parse a large number of Heritrix crawl.log files for line-based processing.
Actually it extracts MIME-Type information, but is extensible. The software
starts at a common parent directory and discovers all crawl.log files recursively.


```bash
java -jar mediatypes.jar --inbound.path=/path/to/harvests --out=mediatypes.csv
```

### Shell-Dashboard

To get a high level view while processing, you can run `src/main/sh/dashboard.sh` in the same directory as your processing log file:

```bash
./dashboard.sh
```

### Consolidation of Media Types

Many of the gathered Media Types are invalid, e.g. because of typos. In `mappings.yml` one defines a mapping from wrong to consolidated Media Types (see example in `src/main/python`). The mapping has to be in the same directory as `consolidate.py`.


```bash
python consolidate.py mediatypes.csv consolidated.csv
```

## Development

To build the software, use

```bash
mvn clean install
```
