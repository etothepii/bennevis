package uk.co.epii.bennevis.opendata;

import uk.co.epii.bennevis.TestData;

import java.io.IOException;

/**
 * User: James Robinson
 * Date: 27/03/2015
 * Time: 18:13
 */
public class AltitudeLoader {

  public static double[] loadAltitudesFromCSVData(String file) {
    String[][] rawPoints;
    try {
      rawPoints = TestData.loadCsv(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    double[] points = new double[rawPoints.length];
    for (int i = 0; i < points.length; i++) {
      points[i] = Double.parseDouble(rawPoints[i][0]);
    }
    return points;
  }

}
