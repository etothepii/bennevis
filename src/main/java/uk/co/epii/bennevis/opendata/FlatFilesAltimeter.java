package uk.co.epii.bennevis.opendata;

import uk.co.epii.bennevis.IAltimeter;
import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 23:59
 */
public class FlatFilesAltimeter implements IAltimeter {

  private Interpolator interpolator;
  private Terrain50 terrain50;

  public FlatFilesAltimeter() {
    this.terrain50 = new Terrain50FlatFileImpl();
    this.interpolator = new WeightedMaxDistanceInterpolator();
  }

  public FlatFilesAltimeter(Terrain50 terrain50, Interpolator interpolator) {
    this.terrain50 = terrain50;
    this.interpolator = interpolator;
  }

  @Override
  public double getAltitude(OSRef osref) {
    AltitudePointLocation[] altitudeLocation = AltitudePointLocation.cornersFromOSRef(osref);
    double topLeft = terrain50.getAltitude(altitudeLocation[0]);
    double topRight = terrain50.getAltitude(altitudeLocation[1]);
    double bottomLeft = terrain50.getAltitude(altitudeLocation[2]);
    double bottomRight = terrain50.getAltitude(altitudeLocation[3]);
    double horizontalRatio = (osref.getEasting() - altitudeLocation[0].getEasting()) / 50d;
    double verticalRatio = (osref.getNorthing() - altitudeLocation[0].getNorthing()) / 50d;
    return interpolator.interpolate(topLeft, topRight, bottomLeft, bottomRight, horizontalRatio, verticalRatio);
  }
}
