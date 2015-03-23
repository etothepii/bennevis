package uk.co.epii.bennevis.opendata;

import org.junit.Test;
import uk.me.jstott.jcoord.OSRef;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:28
 */
public class AltitudeLocationTests {

  @Test
  public void canCreateAltitude() {
    AltitudeLocation result = AltitudeLocation.fromOSRef(new OSRef(250549d, 103498d));
    assertEquals("LargeSquare", "SS", result.getLargeSquare());
    assertEquals("SmallSquare", "50", result.getSmallSquare());
    assertEquals("Row", 188, result.getRow());
    assertEquals("Column", 70, result.getCol());
  }

}
