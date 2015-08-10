package uk.co.epii.bennevis.opendata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: jrrpl
 * Date: 27/03/2015
 * Time: 18:04
 */
public abstract class AbstractTerrain50 implements Terrain50 {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractTerrain50.class);

  @Override
  public double getAltitude(AltitudeLocation altitudeLocation) {
    File file = new File(getRootDataFolder() + altitudeLocation.getLargeSquare().toLowerCase());
    LOG.debug("Seeking files at: " + file);
    File[] possibleZips = file.listFiles(altitudeLocation);
    if (possibleZips.length > 1) {
      LOG.warn("More files found than expected defualting to first");
    }
    if (possibleZips.length == 0) {
      LOG.warn("No file found");
      return 0;
    }
    return getAltitude(possibleZips[0], altitudeLocation);
  }

  protected abstract String getRootDataFolder();

  protected abstract double getAltitude(File possibleZip, AltitudeLocation altitudeLocation);
}
