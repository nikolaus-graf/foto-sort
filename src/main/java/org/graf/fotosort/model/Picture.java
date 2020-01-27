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

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private final File path;

  public Picture(File path) {
    this.path = path;
  }

  public File getPath() {
    return path;
  }

  public void copyTo(String name, File dir) {
    try {
      copyTo(name, dir, null);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void copyTo(String name, File dir, Integer suffix) throws IOException {
    String nameWithoutExtension = name.substring(0, name.lastIndexOf("."));
    String extension = name.substring(name.lastIndexOf("."));

    String finalName = nameWithoutExtension + (isNull(suffix) ? "" : "_" + suffix) + extension;
    File destinationFile = new File(dir, finalName);

    if (destinationFile.exists()) {
      if (buildHash(path).equals(buildHash(destinationFile))) {
        logger.info("file already exist with same hash: {}", finalName);
        return;
      }
      copyTo(name, dir, (isNull(suffix) ? 1 : suffix + 1));
    } else {
      copyFile(path, destinationFile, true);
      logger.info("copied file {} to {}", path.getAbsolutePath(), destinationFile.getAbsolutePath());
    }
  }

  private String buildHash(File file) throws IOException {
    byte[] bytes = readAllBytes(get(file.toURI()));
    return md5DigestAsHex(bytes);
  }
}
