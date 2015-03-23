package uk.co.epii.bennevis.opendata;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 19:22
 */
public interface Interpolator {

  public double interpolate(
          double topLeft,
          double topRight,
          double bottomLeft,
          double bottomRight,
          double horizontalRatio,
          double verticalRatio);

}
