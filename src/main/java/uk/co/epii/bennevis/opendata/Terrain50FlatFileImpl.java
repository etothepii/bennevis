package uk.co.epii.bennevis.opendata;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 12:05
 */
public class Terrain50FlatFileImpl extends AbstractTerrain50 {

  private static final Logger LOG = LoggerFactory.getLogger(Terrain50FlatFileImpl.class);

  private final Map<File, double[][]> loadedFiles = new HashMap<File, double[][]>();

  @Override
  protected String getRootDataFolder() {
    return DataProperties.POINTS_FOLDER;
  }

  public double getAltitude(File zip, AltitudeLocation altitudeLocation) {
    int row = 0; int col = 0;
    if (altitudeLocation instanceof AltitudePointLocation) {
      AltitudePointLocation altitudePointLocation = (AltitudePointLocation)altitudeLocation;
      row = altitudePointLocation.getRow();
      col = altitudePointLocation.getCol();
    }
    return getAltitude(zip, row, col);

  }

  private double getAltitude(File zip, int row, int col) {
    double[][] altitudes = loadedFiles.get(zip);
    if (altitudes == null) {
      altitudes = loadZip(zip);
      loadedFiles.put(zip, altitudes);
    }
    if (altitudes.length == 0) {
      return 0;
    }
    return altitudes[row][col];
  }

  private double[][] loadZip(File zip) {
    try {
      ZipFile zipFile = new ZipFile(zip);
      for (Object fileHeaderObj : zipFile.getFileHeaders()) {
        FileHeader fileHeader = (FileHeader)fileHeaderObj;
        String fileName = fileHeader.getFileName().toUpperCase();
        if (fileName.endsWith(".ASC")) {
          InputStream inputStream = zipFile.getInputStream(fileHeader);
          return loadFile(inputStream);
        }
      }
    }
    catch (Exception e) {
      LOG.warn("An Exception has occured loading data from the zip file", e);
    }
    return new double[0][];
  }

  private double[][] loadFile(InputStream inputStream) throws IOException {
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    String in;
    try {
      inputStreamReader = new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(inputStreamReader);
      int line = 0;
      double[][] result = new double[200][];
      while ((in = bufferedReader.readLine()) != null) {
        if (line >= 5) {
          result[line - 5] = processLine(in);
        }
        line++;
      }
      if (line != 205) {
        throw new RuntimeException(
                String.format("This altitude file is malformated it contains %d rows rather than 200", line - 5));
      }
      return result;
    }
    finally {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (inputStreamReader != null) {
        inputStreamReader.close();
      }
    }
  }

  private double[] processLine(String in) {
    String[] parts = in.split(" ");
    if (parts.length != 200) {
      throw new RuntimeException(
              String.format("This altitude file is malformated it contains %d columns rather than 200", parts.length));
    }
    double[] result = new double[200];
    for (int i = 0; i < 200; i++) {
      result[i] = Double.parseDouble(parts[i]);
    }
    return result;
  }
}
