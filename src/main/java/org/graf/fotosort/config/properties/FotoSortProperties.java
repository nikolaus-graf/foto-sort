package org.graf.fotosort.config.properties;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("foto-sort")
public class FotoSortProperties {

  private List<FotoSortInfo> fotoSortInfoList = new ArrayList<>();

  public List<FotoSortInfo> getFotoSortInfoList() {
    return fotoSortInfoList;
  }

  public void setFotoSortInfoList(List<FotoSortInfo> fotoSortInfoList) {
    this.fotoSortInfoList = fotoSortInfoList;
  }

  @Override
  public String toString() {
    return "FotoSortProperties{" +
        "fotoSortInfoList=" + fotoSortInfoList +
        '}';
  }
}
