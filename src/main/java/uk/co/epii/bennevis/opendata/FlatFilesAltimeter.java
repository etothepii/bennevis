package uk.co.epii.bennevis.opendata;

import uk.co.epii.bennevis.IAltimeter;
import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 23:59
 */
public class FlatFilesAltimeter implements IAltimeter {

  public Terrain50 terrain50 = new Terrain50FlatFileImpl();

  @Override
  public double getAltitude(OSRef osref) {
    AltitudeLocation altitudeLocation = AltitudeLocation.fromOSRef(osref);
    return terrain50.getAltitude(altitudeLocation);
  }
}
