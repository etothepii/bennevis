package uk.co.epii.bennevis.opendata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.epii.bennevis.IAltimeter;
import uk.me.jstott.jcoord.OSRef;

import java.io.File;
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
    return altitudes[row][col];
  }

  private double[][] loadZip(File zip) {
    return new double[0][];
  }
}
