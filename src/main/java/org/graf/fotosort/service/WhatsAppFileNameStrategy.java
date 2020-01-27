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
public class WhatsAppFileNameStrategy implements FileNameStrategy {

  private static final Pattern pattern = compile("IMG-\\d{8}-WA\\d{4}\\.jpg");
  private static final DateTimeFormatter FORMATTER_HHmmss = ofPattern("HHmmss");

  @Override
  public boolean fileNameMatches(File file) {
    return pattern.matcher(file.getName()).matches();
  }

  @Override
  public String buildName(File file) {
    String name = file.getName();
    LocalDateTime lastModified = ofInstant(ofEpochMilli(file.lastModified()), getDefault().toZoneId());
    return name.substring(4, 12) + "_" + lastModified.format(FORMATTER_HHmmss) + ".jpg";
  }
}
