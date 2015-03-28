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

  @Test
  public void canCalculateAltitudes() {
    IAltimeter altimeter = new ContourAltimeter();
    OSRef[] osrefs = GPXLoaderTests.loadPointsFromCSVData("ExampleWalk.csv");
    double[] expected = AltitudeLoader.loadAltitudesFromCSVData("ExampleWalkContourAltitude.csv");
    assertEquals("size of array", expected.length, osrefs.length);
    for (int i = 0; i < expected.length; i++) {
      double result = altimeter.getAltitude(osrefs[i]);
      assertEquals("Point[" + i + "] altitude", expected[i], result, 0.00001);
    }
  }



}
