package uk.co.epii.bennevis.opendata;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.epii.bennevis.IAltimeter;
import uk.me.jstott.jcoord.OSRef;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 23:59
 */
public class FlatFilesAltimeter implements IAltimeter {

  private static final Logger LOG = LoggerFactory.getLogger(FlatFilesAltimeter.class);

  private final Map<File, double[][]> loadedFiles = new HashMap<File, double[][]>();

  private String rootDataFolder = "/Users/jrrpl/Downloads/terr50_gagg_gb/data/";

  @Override
  public double getAltitude(OSRef osref) {
    AltitudeLocation altitudeLocation = AltitudeLocation.fromOSRef(osref);
    File file = new File(rootDataFolder + altitudeLocation.getLargeSquare());
    File[] possibleZips = file.listFiles(altitudeLocation);
    if (possibleZips.length > 1) {
      LOG.warn("More files found than expected defualting to first");
    }
    if (possibleZips.length == 0) {
      LOG.warn("No file found");
      return 0;
    }
    return getAltitude(possibleZips[0], altitudeLocation.getRow(), altitudeLocation.getCol());
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
