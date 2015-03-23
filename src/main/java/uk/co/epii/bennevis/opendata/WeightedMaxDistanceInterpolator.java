package uk.co.epii.bennevis.opendata;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 21:29
 */
public class WeightedMaxDistanceInterpolator implements Interpolator {

  @Override
  public double interpolate(
          double topLeft, double topRight, double bottomLeft,
          double bottomRight, double horizontalRatio, double verticalRatio) {
    double totalWeight = 0d;
    double totalAltitude = 0d;
    double[][] values = new double[][] {
            new double[] {topLeft, horizontalRatio, verticalRatio},
            new double[] {topRight, 1 - horizontalRatio, verticalRatio},
            new double[] {bottomLeft, horizontalRatio, 1 - verticalRatio},
            new double[] {bottomRight, 1 - horizontalRatio, 1 - verticalRatio},
    };
    for (int i = 0; i < 4; i++) {
      double weight = 1 - Math.sqrt(values[i][1] * values[i][1] + values[i][2] * values[i][2]);
      if (weight < 0) {
        continue;
      }
      totalWeight += weight;
      totalAltitude += values[i][0] * weight;
    }
    return totalAltitude / totalWeight;
  }
}
