package org.graf.fotosort.model;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static java.util.Objects.isNull;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Picture {

  private final File path;
  private final String hash;

  public Picture(File path) {
    this.path = path;
    this.hash = buildHash(path);
  }

  private String buildHash(File path) {
    try {
      byte[] bytes = readAllBytes(get(path.toURI()));
      return md5DigestAsHex(bytes);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public File getPath() {
    return path;
  }

  public String getHash() {
    return hash;
  }
}
