package uk.co.epii.bennevis.gpx;

import org.junit.Test;
import uk.co.epii.bennevis.TestData;
import uk.me.jstott.jcoord.OSRef;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 20:03
 */
public class GPXLoaderTests {

  @Test
  public void canLoadGPXFile() {
    GPXLoader gpxLoader = new GPXLoader();
    gpxLoader.loadFile(TestData.load("ExampleWalk.gpx"));
    OSRef[] result = gpxLoader.getPoints();
    double[][] expected = loadPointsFromCSVData("ExampleWalk.csv");
    assertEquals("size of array", expected.length, result.length);
    for (int i = 0; i < result.length; i++) {
      assertEquals("Point[" + i + "] easting", result[i].getEasting(), expected[i][0], 0.00001);
      assertEquals("Point[" + i + "] northing", result[i].getNorthing(), expected[i][1], 0.00001);
    }
  }

  private double[][] loadPointsFromCSVData(String file) {
    String[][] rawPoints;
    try {
      rawPoints = TestData.loadCsv(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    double[][] points = new double[rawPoints.length][];
    for (int i = 0; i < points.length; i++) {
      points[i] = new double[2];
      points[i][0] = Double.parseDouble(rawPoints[i][0]);
      points[i][1] = Double.parseDouble(rawPoints[i][1]);
    }
    return points;
  }
}
