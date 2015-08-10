package uk.co.epii.bennevis.encoder;

import uk.co.epii.bennevis.Altimeter;
import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 10/08/2015
 * Time: 23:30
 */
public class JSONEncoder {

  public String encode(OSRef[] points, Altimeter altimeter) {
    StringBuilder stringBuilder = new StringBuilder(points.length * 200);
    stringBuilder.append("{\"name\":\"Route\",\"points\":[");
    boolean first = true;
    for (OSRef osRef : points) {
      if (first) {
        first = false;
      }
      else {
        stringBuilder.append(",");
      }
      stringBuilder.append(String.format("{\"easting\":%f,\"northing\":%f,\"altitude\":%f}",
              osRef.getEasting(), osRef.getNorthing(), altimeter.getAltitude(osRef)));
    }
    stringBuilder.append("]}");
    return stringBuilder.toString();
  }

}
