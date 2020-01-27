package org.graf.fotosort.service;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.listFiles;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.graf.fotosort.config.properties.FotoSortInfo;
import org.graf.fotosort.config.properties.FotoSortProperties;
import org.graf.fotosort.model.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileSorter {

  private final List<FileNameStrategy> fileNameStrategyList = new ArrayList<>();
  private final FotoSortProperties properties;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public FileSorter(List<FileNameStrategy> fileNameStrategyList, FotoSortProperties properties) {
    this.properties = properties;
    this.fileNameStrategyList.addAll(fileNameStrategyList);
  }

  public void sort() {
    Map<String, Picture> fotoHashMap = new HashMap<>();
    for (FotoSortInfo fotoSortInfo : properties.getFotoSortInfoList()) {
      List<Picture> pictureList = buildPictureList(fotoSortInfo);

      pictureList.forEach(picture -> {
        Optional<Picture> existingPictureOpt = ofNullable(fotoHashMap.get(picture.getHash()));
        if (!existingPictureOpt.isPresent()) {
          copyTo(picture, fotoSortInfo.getTargetDirectory());
          fotoHashMap.put(picture.getHash(), picture);
        } else {
          Picture existingPicture = existingPictureOpt.get();
          logger.info("file already exist with same hash: {} - {}", existingPicture.getPath().getAbsolutePath(), picture.getPath().getAbsolutePath());
        }
      });
    }
  }

  private void copyTo(Picture picture, File dir) {
    try {
      copyTo(picture, renameTo(picture), dir, null);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void copyTo(Picture picture, String name, File dir, Integer suffix) throws IOException {
    String nameWithoutExtension = name.substring(0, name.lastIndexOf("."));
    String extension = name.substring(name.lastIndexOf("."));

    String finalName = nameWithoutExtension + (isNull(suffix) ? "" : "_" + suffix) + extension;
    File destinationFile = new File(dir, finalName);

    if (destinationFile.exists()) {
      copyTo(picture, name, dir, (isNull(suffix) ? 1 : suffix + 1));
    } else {
      copyFile(picture.getPath(), destinationFile, true);
      logger.info("copied file {} to {}", picture.getPath().getAbsolutePath(), destinationFile.getAbsolutePath());
    }
  }

  private List<Picture> buildPictureList(FotoSortInfo fotoSortInfo) {
    return listFiles(fotoSortInfo.getSourceDirectory(), null, true).stream()
        .map(Picture::new)
        .collect(toUnmodifiableList());
  }

  private String renameTo(Picture picture) {
    return findFileNameStrategy(picture)
        .map(fileNameStrategy -> fileNameStrategy.buildName(picture.getPath()))
        .orElse(picture.getPath().getName());
  }

  private Optional<FileNameStrategy> findFileNameStrategy(Picture picture) {
    return fileNameStrategyList.stream()
        .filter(fileNameStrategy -> fileNameStrategy.fileNameMatches(picture.getPath()))
        .findFirst();
  }
}
