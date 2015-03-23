package uk.co.epii.bennevis.opendata;

import org.junit.Test;
import uk.me.jstott.jcoord.OSRef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:28
 */
public class AltitudeLocationTests {

  @Test
  public void canCreateAltitude() {
    AltitudeLocation result = AltitudeLocation.fromOSRef(new OSRef(203498d, 150549d));
    assertEquals("LargeSquare", "SS", result.getLargeSquare());
    assertEquals("SmallSquare", "05", result.getSmallSquare());
    assertEquals("Row", 188, result.getRow());
    assertEquals("Column", 70, result.getCol());
  }

  @Test
  public void canCreateAltitudeCorners() {
    AltitudeLocation[] result = AltitudeLocation.cornersFromOSRef(new OSRef(203498d, 150549d));
    AltitudeLocation[] expected = new AltitudeLocation[] {
            AltitudeLocation.fromOSRef(new OSRef(203450d, 150500d)),
            AltitudeLocation.fromOSRef(new OSRef(203500d, 150500d)),
            AltitudeLocation.fromOSRef(new OSRef(203450d, 150550d)),
            AltitudeLocation.fromOSRef(new OSRef(203500d, 150550d))
    };
    assertEquals("Number of corners", 4, result.length);
    for (int i = 0; i < 4; i++) {
      assertEquals("corner[" + i + "] largeSquare", expected[i].getLargeSquare(), result[i].getLargeSquare());
      assertEquals("corner[" + i + "] smallSquare", expected[i].getSmallSquare(), result[i].getSmallSquare());
      assertEquals("corner[" + i + "] row", expected[i].getRow(), result[i].getRow());
      assertEquals("corner[" + i + "] column", expected[i].getCol(), result[i].getCol());
    }
  }

  @Test
  public void canIdentifyAValidZipFile() {
    AltitudeLocation result = AltitudeLocation.fromOSRef(new OSRef(203498d, 150549d));
    assertTrue(result.accept(null, "ss05_OST50GRID_20130610.zip"));
    assertFalse(result.accept(null, "ss09_OST50GRID_20130610.zip"));
  }

}
