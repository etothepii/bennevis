package uk.co.epii.bennevis.opendata;

import org.junit.Test;
import uk.co.epii.bennevis.IAltimeter;
import uk.co.epii.bennevis.TestData;
import uk.co.epii.bennevis.gpx.GPXLoaderTests;
import uk.me.jstott.jcoord.OSRef;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:02
 */
public class FlatFilesAltimeterTests {

  @Test
  public void canCalculateAltitudes() {
    IAltimeter altimeter = new FlatFilesAltimeter();
    OSRef[] osrefs = GPXLoaderTests.loadPointsFromCSVData("ExampleWalk.csv");
    double[] expected = loadAltitudesFromCSVData("ExampleWalkAltitude.csv");
    assertEquals("size of array", expected.length, osrefs.length);
    for (int i = 0; i < expected.length; i++) {
      double result = altimeter.getAltitude(osrefs[i]);
      assertEquals("Point[" + i + "] altitude", result, expected[i], 0.00001);
    }
  }

  private double[] loadAltitudesFromCSVData(String file) {
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
