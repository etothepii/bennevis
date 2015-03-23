package uk.co.epii.bennevis.opendata;

import uk.co.epii.bennevis.IAltimeter;
import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 23:59
 */
public class FlatFilesAltimeter implements IAltimeter {


  public Terrain50 terrain50;

  public FlatFilesAltimeter() {
    this.terrain50 = new Terrain50FlatFileImpl();
  }

  public FlatFilesAltimeter(Terrain50 terrain50) {
    this.terrain50 = terrain50;
  }

  @Override
  public double getAltitude(OSRef osref) {
    AltitudeLocation[] altitudeLocation = AltitudeLocation.cornersFromOSRef(osref);
    double divisor = 0d;
    double altitude = 0d;
    for (int i = 0; i < 4; i++) {
      double d_x = altitudeLocation[i].getEasting() - osref.getEasting();
      double d_y = altitudeLocation[i].getNorthing() - osref.getNorthing();
      double weight = Math.max(0d, 50d - Math.sqrt(d_x * d_x + d_y * d_y));
      altitude += terrain50.getAltitude(altitudeLocation[i]) * weight;
      divisor += weight;
    }
    return altitude / divisor;
  }
}
