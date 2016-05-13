package de.digitalcollections.webarchive.mediatypes.analysis.integration.transformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class FileToLineTransformer implements GenericTransformer<File, Iterator<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileToLineTransformer.class);

  /**
   * Returns an iterator that reads and returns the file line by line.
   *
   * @param file The file to read and split into lines.
   * @return An iterator over the lines of file.
   */
  @Override
  public Iterator<String> transform(File file) {
    try {
      return new LineIterator(new FileReader(file));
    } catch (FileNotFoundException e) {
      LOGGER.error("Could not read file", e);
    }
    return Collections.emptyIterator();
  }
}
