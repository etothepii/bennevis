package uk.co.epii.bennevis.opendata;

import uk.co.epii.bennevis.Altimeter;
import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 24/03/2015
 * Time: 00:11
 */
public class ContourAltimeter implements Altimeter {

  Terrain50 terrain50 = new Terrain50ContourImpl();

  @Override
  public double getAltitude(OSRef osref) {
    AltitudeLocation altitudeLocation = AltitudeLocation.fromOSRef(osref);
    return terrain50.getAltitude(altitudeLocation);
  }

}
