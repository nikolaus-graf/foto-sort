package org.graf.fotosort.service;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.io.FileUtils.listFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.graf.fotosort.config.properties.FotoSortInfo;
import org.graf.fotosort.config.properties.FotoSortProperties;
import org.graf.fotosort.model.Picture;
import org.springframework.stereotype.Service;

@Service
public class FileSorter {

  private final List<FileNameStrategy> fileNameStrategyList = new ArrayList<>();
  private final FotoSortProperties properties;

  public FileSorter(List<FileNameStrategy> fileNameStrategyList, FotoSortProperties properties) {
    this.properties = properties;
    this.fileNameStrategyList.addAll(fileNameStrategyList);
  }

  public void sort() {
    for (FotoSortInfo fotoSortInfo : properties.getFotoSortInfoList()) {
      List<Picture> pictureList = listFiles(fotoSortInfo.getSourceDirectory(), null, true).stream()
          .map(Picture::new)
          .collect(toUnmodifiableList());

      pictureList.forEach(picture -> picture.copyTo(renameTo(picture), fotoSortInfo.getTargetDirectory()));
    }
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
