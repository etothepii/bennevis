package uk.co.epii.bennevis.opendata;

import org.junit.Test;
import uk.co.epii.bennevis.IAltimeter;
import uk.co.epii.bennevis.TestData;
import uk.co.epii.bennevis.gpx.GPXLoaderTests;
import uk.me.jstott.jcoord.OSRef;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:02
 */
public class FlatFilesAltimeterTests {

  @Test
  public void canCalculateAltitude1() {
    Map<Point, Double> heights = new HashMap<Point, Double>();
    heights.put(new Point(203450,150500), 20d);
    heights.put(new Point(203500,150500), 30d);
    heights.put(new Point(203450,150550), 40d);
    heights.put(new Point(203500,150550), 50d);
    IAltimeter altimeter = new FlatFilesAltimeter(new DummyTerrain50(heights));
    double result = altimeter.getAltitude(new OSRef(203498, 150549));
    double expected = 49.233;
    assertEquals("Calculated Altitude", expected, result, 0.01);
  }

  @Test
  public void canCalculateAltitude2() {
    Map<Point, Double> heights = new HashMap<Point, Double>();
    heights.put(new Point(203450,150500), 0d);
    heights.put(new Point(203500,150500), 100d);
    heights.put(new Point(203450,150550), 200d);
    heights.put(new Point(203500,150550), 300d);
    IAltimeter altimeter = new FlatFilesAltimeter(new DummyTerrain50(heights));
    double result = altimeter.getAltitude(new OSRef(203475, 150525));
    double expected = 150;
    assertEquals("Calculated Altitude", expected, result, 0.01);
  }

  @Test
  public void canCalculateAltitudes() {
    IAltimeter altimeter = new FlatFilesAltimeter();
    OSRef[] osrefs = GPXLoaderTests.loadPointsFromCSVData("ExampleWalk.csv");
    double[] expected = loadAltitudesFromCSVData("ExampleWalkAltitude.csv");
    //assertEquals("size of array", expected.length, osrefs.length);
    for (int i = 0; i < expected.length; i++) {
      double result = altimeter.getAltitude(osrefs[i]);
      //assertEquals("Point[" + i + "] altitude", result, expected[i], 0.00001);
      System.out.println(result);
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
