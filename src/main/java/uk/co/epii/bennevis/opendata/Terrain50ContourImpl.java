package uk.co.epii.bennevis.opendata;

import java.io.File;

/**
 * User: James Robinson
 * Date: 27/03/2015
 * Time: 18:01
 */
public class Terrain50ContourImpl extends AbstractTerrain50 {

  private String rootDataFolder = "/Users/jrrpl/Downloads/terr50_cesh/data/";

  @Override
  protected String getRootDataFolder() {
    return rootDataFolder;
  }

  @Override
  protected double getAltitude(File zip, AltitudeLocation altitudeLocation) {
    return 0;
  }
}
