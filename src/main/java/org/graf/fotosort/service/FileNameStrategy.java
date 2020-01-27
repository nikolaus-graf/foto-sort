package org.graf.fotosort.service;

import java.io.File;

public interface FileNameStrategy {

  boolean fileNameMatches(File file);

  String buildName(File file);
}
