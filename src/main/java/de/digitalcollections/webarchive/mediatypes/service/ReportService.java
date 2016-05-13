package de.digitalcollections.webarchive.mediatypes.service;

import de.digitalcollections.webarchive.mediatypes.model.MediaTypeInfo;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

  private static final String SEPARATOR = ";";

  public void writeFile(String filename, Map<String, MediaTypeInfo> mimeTypeCounts) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("mime.csv"))) {
      write(writer, mimeTypeCounts);
    } catch (IOException exception) {
      LOGGER.error("Could not write to file", exception);
    }
  }

  public void write(Writer writer, Map<String, MediaTypeInfo> mediaTypeCounts) throws IOException {
    for (String key : mediaTypeCounts.keySet()) {
      MediaTypeInfo mimeInfo = mediaTypeCounts.get(key);
      writeLine(writer, key, mimeInfo.getCount(), mimeInfo.getSize());
    }
  }

  public void writeLine(Writer writer, String mediaType, long count, long size) throws IOException {
    writer.write(mediaType);
    writer.write(";");
    writer.write(Long.toString(count));
    writer.write(";");
    writer.write(Long.toString(size));
    writer.write("\n");
  }

}
