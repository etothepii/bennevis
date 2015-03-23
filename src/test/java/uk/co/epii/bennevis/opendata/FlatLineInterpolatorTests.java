package uk.co.epii.bennevis.opendata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 20:16
 */
public class FlatLineInterpolatorTests {

  @Test
  public void canCalculateAltitude1() {
    FlatLineInterpolator flatLineInterpolator = new FlatLineInterpolator();
    double result = flatLineInterpolator.interpolate(20d, 30d, 40d, 50d, .96, .98);
    double expected = 49.0;
    assertEquals("Calculated Altitude", expected, result, 0.000001);
  }

  @Test
  public void canCalculateAltitude2() {
    FlatLineInterpolator flatLineInterpolator = new FlatLineInterpolator();
    double result = flatLineInterpolator.interpolate(0d, 100d, 200d, 300d, .5, .5);
    double expected = 150;
    assertEquals("Calculated Altitude", expected, result, 0.0000001);
  }
}
