package org.graf.fotosort.service;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.TimeZone.getDefault;
import static java.util.regex.Pattern.compile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class KameraFileNameStrategy implements FileNameStrategy {

  private static final Pattern pattern = compile("DSC\\d{5}\\.JPG");
  private static final DateTimeFormatter FORMATTER_YYYYMMDD_HHmmss = ofPattern("YYYYMMDD_HHmmss");

  @Override
  public boolean fileNameMatches(File file) {
    return pattern.matcher(file.getName()).matches();
  }

  @Override
  public String buildName(File file) {
    LocalDateTime lastModified = ofInstant(ofEpochMilli(file.lastModified()), getDefault().toZoneId());
    return lastModified.format(FORMATTER_YYYYMMDD_HHmmss) + ".jpg";
  }
}
