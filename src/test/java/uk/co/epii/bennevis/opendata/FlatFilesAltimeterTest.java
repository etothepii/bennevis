package uk.co.epii.bennevis.opendata;

import org.junit.Ignore;
import org.junit.Test;
import uk.co.epii.bennevis.Altimeter;
import uk.co.epii.bennevis.gpx.GPXLoaderTest;
import uk.me.jstott.jcoord.OSRef;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:02
 */
public class FlatFilesAltimeterTest {

  @Test
  @Ignore
  public void canCalculateAltitudes() {
    Altimeter altimeter = new FlatFilesAltimeter();
    OSRef[] osrefs = GPXLoaderTest.loadPointsFromCSVData("oldExampleWalk.csv");
    double[] expected = AltitudeLoader.loadAltitudesFromCSVData("ExampleWalkAltitude.csv");
    assertEquals("size of array", expected.length, osrefs.length);
    for (int i = 0; i < expected.length; i++) {
      double result = altimeter.getAltitude(osrefs[i]);
      assertEquals("Point[" + i + "] altitude", expected[i], result, 0.00001);
    }
  }

}
