package uk.co.epii.bennevis.opendata;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 19:45
 */
public class FlatLineInterpolator implements Interpolator {


  @Override
  public double interpolate(double topLeft, double topRight, double bottomLeft, double bottomRight, double horizontalRatio, double verticalRatio) {

    return topLeft * (1 - verticalRatio) * (1 - horizontalRatio) +
            bottomLeft * horizontalRatio * (1 - verticalRatio) +
            topRight * verticalRatio * (1 - horizontalRatio) +
            bottomRight * horizontalRatio * verticalRatio;
  }
}
