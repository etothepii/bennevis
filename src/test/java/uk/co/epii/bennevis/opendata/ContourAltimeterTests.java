package uk.co.epii.bennevis.opendata;

import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.epii.bennevis.IAltimeter;
import uk.co.epii.bennevis.gpx.GPXLoaderTests;
import uk.me.jstott.jcoord.OSRef;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:02
 */
public class ContourAltimeterTests {

  private static IAltimeter altimeter;

  @BeforeClass
  public static void beforeClass() {
    altimeter = new ContourAltimeter();
  }

  @Test
  public void canCalculateAltitude1() {
    OSRef osRef = new OSRef(271719d, 149514d);
    double expected = 170d;
    double result = altimeter.getAltitude(osRef);
    assertEquals(expected, result, 1d);
  }

  @Test
  public void canCalculateAltitudes() {
    IAltimeter altimeter = new ContourAltimeter();
    OSRef[] osrefs = GPXLoaderTests.loadPointsFromCSVData("ExampleWalk.csv");
    double[] expected = AltitudeLoader.loadAltitudesFromCSVData("ExampleWalkContourAltitude.csv");
    double result = altimeter.getAltitude(new OSRef(271719d, 149514d));
    assertEquals("size of array", expected.length, osrefs.length);
    for (int i = 0; i < expected.length; i++) {
      result = altimeter.getAltitude(osrefs[i]);
      System.out.println(osrefs[i].getEasting() + "," + osrefs[i].getNorthing() + "," + result);
      //assertEquals("Point[" + i + "] altitude", expected[i], result, 0.00001);
    }
  }



}
