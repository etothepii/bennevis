package uk.co.epii.bennevis.opendata;

import uk.co.epii.bennevis.IAltimeter;
import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 23:59
 */
public class FlatFilesAltimeter implements IAltimeter {

  private String rootDataFolder = "/Users/jrrpl/Downloads/terr50_gagg_gb/data/";

  @Override
  public double getAltitude(OSRef osref) {
    return 0;
  }
}
