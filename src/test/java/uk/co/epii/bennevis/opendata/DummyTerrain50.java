package uk.co.epii.bennevis.opendata;

import java.awt.*;
import java.util.Map;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 13:06
 */
public class DummyTerrain50 implements Terrain50 {

  private final Map<Point, Double> heights;

  public DummyTerrain50(Map<Point, Double> heights) {
    this.heights = heights;
  }

  @Override
  public double getAltitude(AltitudeLocation altitudeLocation) {
    Double height = heights.get(new Point(altitudeLocation.getEasting(), altitudeLocation.getNorthing()));
    if (height == null) {
      return 0;
    }
    return height;
  }
}
