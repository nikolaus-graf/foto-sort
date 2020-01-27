package org.graf.fotosort.config.properties;

import java.io.File;

public class FotoSortInfo {

  private File sourceDirectory;
  private File targetDirectory;

  public File getSourceDirectory() {
    return sourceDirectory;
  }

  public void setSourceDirectory(File sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public File getTargetDirectory() {
    return targetDirectory;
  }

  public void setTargetDirectory(File targetDirectory) {
    this.targetDirectory = targetDirectory;
  }

  @Override
  public String toString() {
    return "FotoSortInfo{" +
        "sourceDirectory=" + sourceDirectory +
        ", targetDirectory=" + targetDirectory +
        '}';
  }
}
