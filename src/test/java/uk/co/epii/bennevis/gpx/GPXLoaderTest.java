package uk.co.epii.bennevis.gpx;

import org.junit.Ignore;
import org.junit.Test;
import uk.co.epii.bennevis.TestData;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 20:03
 */
public class GPXLoaderTest {

  @Test
  public void canLoadGPXFile() {
    GPXLoader gpxLoader = new GPXLoader();
    gpxLoader.loadFile(TestData.load("ExampleWalk.gpx"));
    OSRef[] result = gpxLoader.getPoints();
    OSRef[] expected = loadPointsFromCSVData("ExampleWalk.csv");
    assertEquals("size of array", expected.length, result.length);
    for (int i = 0; i < result.length; i++) {
      assertEquals("Point[" + i + "] easting", result[i].getEasting(), expected[i].getEasting(), 0.00001);
      assertEquals("Point[" + i + "] northing", result[i].getNorthing(), expected[i].getNorthing(), 0.00001);
    }
  }

  @Test
  public void interpolationOnlyInterpolates() {
    GPXLoader gpxLoader = new GPXLoader();
    gpxLoader.loadFile(TestData.load("ExampleWalk.gpx"));
    OSRef[] result = gpxLoader.getPoints();
    OSRef[] expected = loadPointsFromCSVData("oldExampleWalk.csv");
    int vertex = 0;
    for (int i = 0; vertex < expected.length && i < result.length; i++) {
      if (Math.abs(result[i].getEasting() - expected[vertex].getEasting()) < 0.01
              && Math.abs(result[i].getNorthing() - expected[vertex].getNorthing()) < 0.01) {
        vertex++;
      }
    }
    assertEquals("Vertecies present", expected.length, vertex);
  }

  @Test
  public void canLoadViewRangerGPXFile() {
    GPXLoader gpxLoader = new GPXLoader();
    gpxLoader.loadFile(TestData.load("ViewRangerWalk.gpx"));
    OSRef[] result = gpxLoader.getPoints();
    OSRef[] expected = loadPointsFromCSVData("ViewRangerWalk.csv");
    assertEquals("Pennine Way - Day 07 - Alston to Garrigill", gpxLoader.getName());
    assertEquals("size of array", expected.length, result.length);
    for (int i = 0; i < result.length; i++) {
      assertEquals("Point[" + i + "] easting", result[i].getEasting(), expected[i].getEasting(), 0.00001);
      assertEquals("Point[" + i + "] northing", result[i].getNorthing(), expected[i].getNorthing(), 0.00001);
    }
  }

  @Test
  @Ignore
  public void testSwitchingBetweenLongLatAndOS() {
    double longitude = -3.838392;
    double latitude = 51.230681;
    double northing = 149550;
    double easting = 271739;
    LatLng latLng = new LatLng(latitude, longitude);
    latLng.toOSGB36();
    OSRef osref = latLng.toOSRef();
    assertEquals("Northing", northing, osref.getNorthing(), 1);
    assertEquals("Easting", easting, osref.getEasting(), 1);
  }

  public static OSRef[] loadPointsFromCSVData(String file) {
    String[][] rawPoints;
    try {
      rawPoints = TestData.loadCsv(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    OSRef[] points = new OSRef[rawPoints.length];
    for (int i = 0; i < points.length; i++) {
      points[i] = new OSRef(Double.parseDouble(rawPoints[i][0]), Double.parseDouble(rawPoints[i][1]));
    }
    return points;
  }
}
