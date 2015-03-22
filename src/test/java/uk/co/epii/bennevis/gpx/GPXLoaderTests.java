package uk.co.epii.bennevis.gpx;

import org.junit.Test;
import uk.co.epii.bennevis.TestData;

import java.awt.geom.Point2D;
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
    Point2D.Float[] result = gpxLoader.getPoints();
    Point2D.Float[] expected = loadPointsFromCSVData("ExampleWalk.csv");
    assertEquals("size of array", expected.length, result.length);
    for (int i = 0; i < result.length; i++) {
      assertEquals("Point[" + i + "].x", result[i].x, expected[i].x, 0.1);
      assertEquals("Point[" + i + "].y", result[i].y, expected[i].y, 0.1);
    }
  }

  private Point2D.Float[] loadPointsFromCSVData(String file) {
    String[][] rawPoints;
    try {
      rawPoints = TestData.loadCsv(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Point2D.Float[] points = new Point2D.Float[rawPoints.length];
    for (int i = 0; i < points.length; i++) {
      points[i] = new Point2D.Float(Float.parseFloat(rawPoints[i][0]), Float.parseFloat(rawPoints[i][1]));
    }
    return points;
  }
}
