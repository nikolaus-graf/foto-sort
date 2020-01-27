package org.graf.fotosort;

import java.io.File;
import org.graf.fotosort.config.properties.FotoSortProperties;
import org.graf.fotosort.service.FileSorter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(FotoSortProperties.class)
public class FotoSortApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(FotoSortApplication.class, args);

    FileSorter fileSorter = applicationContext.getBean(FileSorter.class);

    fileSorter.sort();
  }
}
